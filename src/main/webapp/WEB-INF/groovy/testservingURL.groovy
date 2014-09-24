import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobKey
import org.apache.commons.io.IOUtils
import org.apache.commons.fileupload.util.Streams
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.xaymaca.appengine.BlobCreator
import com.xaymaca.appengine.Persister


def blobkey = new BlobKey(blobKeyString)

def creator = new BlobCreator()
ImagesService imageService = ImagesServiceFactory.getImagesService()
int rez = params.rez
boolean crop = params.crop

log.info "imageService.getServingUrl(blob) is getting " + blobkey.getClass()
  
def urlString = "http://lh3.ggpht.com/${blobKeyString}"
def urlSizeParam = "=s32"
def urlcropParam = "-c"
def url = new URL(urlString+urlSizeParam)
InputStream is = url.openStream()

byte[] imageArray = IOUtils.toByteArray(is)
def image = imageArray.image

//response.setHeader("Content-Disposition","attachment;filename=${imageKey}.${imgType.split("/")[1] }")

String imgType = image.getFormat()
        def mime = "image/" + imgType.toLowerCase()

def lucky       = image.rotate(90)

        def newKey = creator.addBlob("id","george",imageArray, mime )
        if(newKey != null ) {
            // log.info "we are good.  " +  " new key " + newKey
            
            Persister p = new Persister(newKey)
            uuid = p.persist()
            // set response type
            response.setContentType( "text/xml" )

            // render image out
            //out << pic.imageData
 
            def responseXML = """<?xml version="1.0" encoding="UTF-8"?>
    <image>
    <key>${uuid}</key>
    <fileURL>/image/${uuid}/</fileURL> 
    </image>   
    """
            out << responseXML
            
            
        }




//blob.serve response


// println "url "  + imageService.getServingUrl(blobkey, rez, crop)


