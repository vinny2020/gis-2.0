import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.blobstore.BlobstoreService
import com.google.appengine.api.blobstore.BlobstoreServiceFactory
import com.xaymaca.appengine.BlobCreator
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.images.Image;
import com.xaymaca.appengine.IdExchange
import com.xaymaca.appengine.Persister
import com.xaymaca.appengine.BlobCreator
import org.apache.commons.io.IOUtils





//def imageService = ImagesServiceFactory.getImagesService()
def creator = new BlobCreator()

def imageKey = params.imgKey
def urlKey = request.getQueryString()
 

def idx = new IdExchange()
def blobKeyString = idx.idToBlob(imageKey)



boolean crop = params.crop
def blobkey = new BlobKey(blobKeyString)


log.info "the imageKey is "  + imageKey
log.info "the blobKey is "  + blobKeyString
log.info  "the server url is is " + images.getServingUrl(blobkey)
def urlString = images.getServingUrl(blobkey)


//<transform resolution="40" crop="true"/>
Integer rez = Integer.valueOf(params.rez)


StringBuilder sb = new StringBuilder()
  
//def urlString = "http://lh3.ggpht.com/${blobKeyString}"
//def urlString = "http://0.0.0.0:8080/_ah/img/${blobKeyString}"

sb.append(urlString)

def urlSizeParam 
def urlcropParam

if(rez != null) {
    urlSizeParam = "=s${rez}" 
    sb.append(urlSizeParam)
    
    if(crop) {
        urlcropParam = "-c"
        sb.append(urlcropParam)
    }
    
}
 
log.info "final url " + sb.toString()
def url = new URL(sb.toString())
InputStream is = url.openStream()

byte[] imageArray = IOUtils.toByteArray(is)
def image = imageArray.image





//response.setHeader("Content-Disposition","attachment;filename=${imageKey}.${imgType.split("/")[1] }")

String imgType = image.getFormat()
def mime = "image/" + imgType.toLowerCase()



Map newKeyMap = creator.addBlob(urlKey,"george",imageArray, mime )
if(newKeyMap != null ) {
     //log.info "we are good.  " +  " new key " + newKey

     def newKey = newKeyMap.get("blobKey") ;
     def isCached = newKeyMap.get("cached") ;


    if(!isCached) {
    Persister p = new Persister(newKey)
    uuid = p.persist()
    }
    else{
    uuid = idx.blobToId(newKey);


    }
            

    // set response type
    response.setContentType( "text/xml" )

        
 
    def responseXML = """<?xml version="1.0" encoding="UTF-8"?>
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
    sb.append("<errors>\n");
    sb.append("<error type=\"timeout\">\n");
    sb.append("<error message=\"Timed Out, URLFetch exceeded 10 seconds\">\n");
    sb.append("<errors>\n");
                 
    out << errorXml.toString()
                 
}



