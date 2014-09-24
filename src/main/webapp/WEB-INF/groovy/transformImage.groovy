import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.images.CompositeTransform
import com.google.appengine.api.images.ImagesService
import com.google.appengine.api.images.ImagesService.OutputEncoding
import com.google.appengine.api.images.ImagesServiceFactory
import com.xaymaca.appengine.BlobCreator
import com.xaymaca.appengine.IdExchange
import com.xaymaca.appengine.Persister

//log.info   "our xml in here? "   + request.getAttributeNames().dump()

def xml = request.getInputStream().getText()

//log.info "the grail? "  + xml






def transformations =new XmlSlurper().parseText(xml)
def allTransformations = transformations.transform
log.info " this many "  + allTransformations.size()
def imageKey = transformations.image.@key.toString()
String encodeString = transformations.image.@encodeAs.toString()
Enum encodeAs 

if(encodeString != null && encodeString != "") {
    if(encodeString.toUpperCase() =="PNG") encodeAs =  OutputEncoding.PNG
    if(encodeString.toUpperCase() =="JPEG") encodeAs =  OutputEncoding.JPEG
   // log.info "encodeAs is a " + encodeAs.getClass()
}


def idx = new IdExchange()
def blobKeyString = idx.idToBlob(imageKey)
        
def blob = new BlobKey(blobKeyString)
def image = images.makeImageFromBlob(blob)  

//log.info "image class is " + image.getClass()

println "the blobKey is "  + blobKeyString
CompositeTransform cp = ImagesServiceFactory.makeCompositeTransform()

for (change in allTransformations ) {
    def transformType =  change.@type
            

            
    /*
    def image = ...
 
    def thumbnail   = image.resize(100, 100)
    def cropped     = image.crop(0.1, 0.1, 0.9, 0.9)
    def hmirror     = image.horizontalFlip()
    def vmirror     = image.verticalFlip()
    def rotated     = image.rotate(90)
    def lucky       = image.imFeelingLucky()
     */
    //do transforms serially
            
    switch(transformType){
                
        case "resize":
        log.info "resize with params: " + change.scale.@height + " and " + change.scale.@width
        def x = new Integer(change.scale[0].@height.toString())
        def y = new Integer(change.scale[0].@width.toString())
        cp.concatenate( ImagesServiceFactory.makeResize(x, y))
        log.info "scaling with resize(${x},${y})" 
        break
                
        case "crop":
        println "crop with params " + change.position.@leftX + " " + change.position.@topY + " " +  change.position.@rightX + " " +  change.position.@bottomY 
        Double leftX = Double.valueOf(change.position.@leftX.toString())
        Double topY = Double.valueOf(change.position.@topY.toString())
        Double rightX = Double.valueOf(change.position.@rightX.toString())
        Double bottomY = Double.valueOf(change.position.@bottomY.toString())
        
        cp.concatenate(ImagesServiceFactory.makeCrop( leftX, topY, rightX , bottomY))
        log.info "crop with leftX:  ${leftX} topY: ${topY} rightX: ${rightX} bottomY: ${bottomY}"
        break
                
        case "rotate":
        println "rotate with params: " + change.angle.@degrees 
        cp.concatenate(ImagesServiceFactory.makeRotate(Integer.valueOf(change.angle.@degrees.toString())))
        break
                
        case "flip":               
        println "flip with  " + change.orientation.@direction   
        if(change.orientation.@direction =="vertical" )    cp.concatenate(ImagesServiceFactory.makeVerticalFlip())
        if(change.orientation.@direction =="horizontal" )  cp.concatenate(ImagesServiceFactory.makeHorizontalFlip())
        break
                
        case "random":               
        println  change.enhance.@type  
        cp.concatenate(ImagesServiceFactory.makeImFeelingLucky())
        break
   
        default:
        println "Don't know"
    }
            
    //save back to blobStore so we can have a perm address to it
         
    

        
        
}
// apply all the transformations to the original image
// images is a Gaelyk shortcut for imageService. Personally, I think it should map to imagesServiceFactory
//log.info "class of comp " +   cp.getClass().toString() + " and image " + image.getClass().toString() + " class"
   
ImagesService service = ImagesServiceFactory.getImagesService()
def compositeimage
   
if(encodeAs != null && encodeAs != '') {
    log.info "encode value is " + encodeAs
    log.info "we are dealing with " + cp.inspect() + " and " + image.getClass() + " and "  + encodeAs.inspect()
    compositeimage = service.applyTransform( cp, image, encodeAs)
}

else{
    compositeimage = service.applyTransform( cp, image )
}
  
   


byte[] binImage   = compositeimage.imageData
    
String imgType = compositeimage.getFormat()
def mime = "image/" + imgType.toLowerCase()
            
def creator = new BlobCreator()  
def newKey = creator.addBlob(blobKeyString,"george",binImage, mime)
    


    if (newKey != null) {
           // log.info "we are good.  " + " new key " + keymap.getClass()

            if (!newKey.get("cached")) {
                Persister p = new Persister(newKey.get("blobKey"))
                uuid = p.persist()
            }
    
    
    // set response type
    response.setContentType( "text/xml" )

 
    def responseXML = """
    <?xml version="1.0" encoding="UTF-8"?>
    <image>
    <key>${uuid}</key>
    <fileURL>/image/${uuid}/</fileURL> 
    </image>
    
    """
    out << responseXML
    }
    else {
         response.status = 500
            StringBuilder errorXml = new StringBuilder();
            sb.append("<?xml version=\" 1.0 \" encoding=\" UTF - 8 \"?>");
            sb.append("<errors>\n");
            sb.append("<error type=\"timeout\">\n");
            sb.append("<error message=\"Timed Out, URLFetch exceeded 10 seconds\">\n");
            sb.append("<errors>\n");

            out << errorXml.toString()


    }



