import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.blobstore.BlobstoreServiceFactory
import com.xaymaca.appengine.BlobCreator



if(params != null ){
    
    
    def blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    
    
    log.info "the params are "  + params.inspect() 
    
   
    
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
            
            def creator = new BlobCreator()  
            def newKey = creator.addBlob(imageKey,"george",binImage)
            if(newKey != null ) {
               log.info "we are good.  " + "oldkey " + imageKey + " new key " + newKey
                response.status = 302
       redirect "/success?key=${newKey}"

            
                        // use httpclient

            
            
            }
            else {
                log.info "key is " + newKey
                sout << "Timeout exceeded? "
            }
            

     
            response.setContentType("image/jpeg") 
            
            //TODO  add image type switch for this

            sout << newImage.imageData

     
        }
        
        	

    
    }
    else {
        log.info "blob key was blank!"
    }

}
    
else {log.info "no params"}
