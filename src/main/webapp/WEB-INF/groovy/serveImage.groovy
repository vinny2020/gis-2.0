/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.blobstore.BlobInfoFactory
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.xaymaca.appengine.IdExchange;



def imageKey = params.imgKey
log.info "we got this param " + imageKey

def idx = new IdExchange()
blobKeyString = idx.idToBlob(imageKey)

log.info "retrieved this blog key " + blobKeyString


def blob = new BlobKey(blobKeyString)
def factory = new BlobInfoFactory()
def info = factory.loadBlobInfo(blob);
log.info "info is " + info
String imgType




if(info != null) {

    imgType = info.getContentType()
    //log.info "content type is " + imgType  + " broken down: " + imgType.toLowerCase().split("/")[1]
    //response.setContentType(imgType.toLowerCase())  
    response.setContentType("image/" + imgType.toLowerCase().split("/")[1] )
}

else {
    imgType = "image/png"
    log.info "no imageinfo? weird using png default"  
    //response.setContentType("image/png")
    response.setContentType("image/png")
    
}
//log.info "header::   attachment;filename=${imageKey}.${imgType.split("/")[1] }"
//response.setHeader("Content-Disposition","inline;filename=${imageKey}.${imgType.split("/")[1] }")

blob.serve response