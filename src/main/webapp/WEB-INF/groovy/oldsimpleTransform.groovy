import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.blobstore.BlobstoreService
import com.google.appengine.api.blobstore.BlobstoreServiceFactory
import com.xaymaca.appengine.BlobCreator
//import groovy.util.XmlSlurper
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.images.CompositeTransform;
import com.xaymaca.appengine.IdExchange
import com.xaymaca.appengine.Persister




def imageService = ImagesServiceFactory.getImagesService()

def imageKey = params.imgKey
 

def idx = new IdExchange()
def blobKeyString = idx.idToBlob(imageKey)



boolean crop = params.crop
def blobkey = new BlobKey(blobKeyString)


log.info "the imageKey is "  + imageKey
log.info "the blobKey is "  + blobKeyString



//<transform resolution="40" crop="true"/>
Integer rez = Integer.valueOf(params.rez) 
  
String url 

log.info "imageService.getServingUrl(blob) is getting " + blobkey.getClass()
  
    url =   imageService.getServingUrl(blobkey)
    log.info "rez and crop are null, got back this url from google " + url   

        

  
def linkedImage = new URL(url);
  

log.info "image key is : " + linkedImage.getPath().split('=')[0].split('/').last()
def newKey = linkedImage.getPath().split('=')[0].split('/').last()
     
// byte[] binImage   =  linkedImage.getBytes()
            
//def creator = new BlobCreator()  
//def newKey = creator.addBlob(blobKeyString,"george",binImage)
    
if(newKey != null ) {
    //log.info "we are good.  " + "oldkey " + blobKeyString + " new key " + newKey
        
    // set response type
        
   
    
    response.setContentType( "text/xml" )

    // render image out
    //sout << pic.imageData
 
    def responseXML = """
    <?xml version="1.0" encoding="UTF-8"?>
    <image>
    <key>${uuid}</key>
    <fileURL>/image/${uuid}/</fileURL> 
    </image>
    
    """
    out << responseXML
}