import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.blobstore.BlobstoreService
import com.google.appengine.api.blobstore.BlobstoreServiceFactory
import com.xaymaca.appengine.BlobCreator
import com.google.appengine.api.datastore.Entity



if(params != null ){
    
    
    def blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    
    
    log.info "the params are "  + params.inspect() 
    
    def imageKey = params.bKey
    def vertflip 
    def horizflip
    def resizeX 
    def resizeY 
    def leftX 
    def topY 
    def rightX 
    def bottomY 
    def rotateDegrees 
    def lucky 

    imageKey = params.bKey
    //def vertflip =
    if(params.vertflip =="on") vertflip = params.vertflip
    if(params.horizflip == "on") horizflip = params.horizflip
    if(params.lucky == "on") lucky = params.lucky
    
    
    if(params.resizeX.isNumber()) resizeX = Integer.valueOf(params.resizeX)
    log.info "resizeX worked"
    if(params.resizeY.isNumber()) resizeY = Integer.valueOf(params.resizeY)
    log.info "resizeY worked"
    if(params.leftX.isNumber()) leftX = Double.valueOf(params.leftX)
    log.info "leftX worked"
    if(params.topY.isNumber()) topY = Double.valueOf(params.topY)
    log.info "topY worked"
    if(params.rightX.isNumber()) rightX = Double.valueOf(params.rightX)
    log.info "rightX worked"
    if(params.bottomY.isNumber()) bottomY = Double.valueOf(params.bottomY)
    log.info "bottomY worked"
    if(params.rotateDegrees != null  && params.rotateDegrees.isNumber() ) rotateDegrees = Integer.valueOf(params.rotateDegrees)
    log.info "rotateDegrees worked"
    
    //response.status = 302


   


    // log.info "the key is " + imageKey
    // log.info "the v flip  is " + vertflip
 
    if(imageKey != null) {
        def blob = new BlobKey(imageKey)

        def newImage =   blob.image.transform {
            if(resizeX != null  && resizeY != null  )   resize resizeX, resizeY
            if(leftX != null  && topY != null  && rightX != null  && bottomY != null)   crop leftX, topY, rightX, bottomY
            if(horizflip != null )   horizontal flip
            if(vertflip != null)   vertical flip
            if(rotateDegrees != null)   rotate rotateDegrees
            if(lucky != null)   feeling lucky
        }
        
        if(newImage != null ) {
            
            // URL Fetch Service
            // Ok, so we get a binary back, add it to the blobstore and redirect
            byte[] binImage   = newImage.imageData
            
            
            String imgType = newImage.getFormat()
            def mime = "image/" + imgType.toLowerCase()
            
            def creator = new BlobCreator() 
            
            def newKey = creator.addBlob(imageKey,"george",binImage, mime)
            if(newKey != null ) {
                //log.info "we are good.  " + "oldkey " + imageKey + " new key " + newKey
                
                def uuid =  UUID.randomUUID().toString()
                //  log.info " uuid is " + uuid
           
                def imageIDs = new Entity("ImageIDs")
                imageIDs.shortID =  uuid
                imageIDs.blobKey = newKey
                imageIDs.save()
                log.info "saving new key in datastore " + newKey
                response.status = 302
                redirect "/success?key=${newKey}"

            
                // use httpclient

            
            
            }
            else {
                log.info "key is " + newKey
                
                StringBuilder sb = new StringBuilder();
                sb.append("<errors>\n");
                sb.append("<error type=\"Timeout\">\n");
                sb.append("<errors>\n");
                //answer = sb.toString();
                //return answer;
                response.status = 500
                
                println  "Timeout exceeded."
            }
            

     
            // response.setContentType("image/jpeg") 
            
            //TODO  add image type switch for this

            // sout << newImage.imageData
            //Hello h = new Hello()
            //println "say " +  h.say()
     
        }
        
        	

    
    }
    else {
        response.status = 500
        log.info "blob key was blank!"
        println "blob key was blank!"
    }

}
    
else {log.info "no params"}
