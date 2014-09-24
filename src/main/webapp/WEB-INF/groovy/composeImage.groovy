import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.images.ImagesServiceFactory
import com.google.appengine.api.images.Composite
import com.xaymaca.appengine.IdExchange
import com.xaymaca.appengine.BlobCreator
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.images.ImagesService.OutputEncoding
import com.google.appengine.api.images.ImagesService
import com.google.appengine.api.images.Composite.Anchor
import com.xaymaca.appengine.Persister




List<Composite> compList = new ArrayList()

def idx = new IdExchange()

def xml = request.getInputStream().getText()

//log.info "the grail? "  + xml






def compose =new XmlSlurper().parseText(xml)
def allCompositions = compose.composite



for (composite in allCompositions ) {
    
    String imageKey = composite.@imgKey.toString() 
    def blobKeyString = new BlobKey(idx.idToBlob(imageKey))
    def image =  images.makeImageFromBlob(blobKeyString)
    int xOffset = Integer.valueOf(composite.@xOffset.toString() ) 
    int yOffset = Integer.valueOf(composite.@yOffset.toString() )
    float opacity = Float.valueOf(composite.@opacity.toString()) 
    def anchorString = composite.@anchor.toString()
    Enum anchor
    if(anchorString != null && anchorString != "") {
        anchor = Composite.Anchor.valueOf(anchorString.toUpperCase())
        log.info "anchor  is a " + anchor.getClass()
    }

    Composite composition = ImagesServiceFactory.makeComposite(image, xOffset, yOffset, opacity, anchor)
    compList.add(composition)
    
}

int width = Integer.valueOf(compose.finalImage.@width.toString())
int height = Integer.valueOf(compose.finalImage.@height.toString())
long color = Long.decode(compose.finalImage.@color.toString()) 
def encodeString = compose.finalImage.@encodeAs.toString()
Enum encodeAs 

if(encodeString != null && encodeString != "") {
    encodeAs = ImagesService.OutputEncoding.valueOf(encodeString.toUpperCase())
   
    log.info "encodeAs is a " + encodeAs.getClass()
}

def newImage

if(encodeAs != null && encodeAs != '') {
    newImage = images.composite(compList, width, height, color, encodeAs )  
}

else {
    
    newImage = images.composite(compList, width, height, color )  
  
}



byte[] binImage   = newImage.imageData
    
String imgType = newImage.getFormat()
def extention = imgType.toLowerCase()
def mime = "image/" + extention
            
def creator = new BlobCreator()  
def newKey = creator.addBlob("new","george",binImage, mime)




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






