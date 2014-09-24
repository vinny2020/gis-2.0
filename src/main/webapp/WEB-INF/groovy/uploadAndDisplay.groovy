import com.google.appengine.api.datastore.Entity

def blobs = blobstore.getUploadedBlobs(request)
def blobKey = blobs["george"]

uuid =  UUID.randomUUID().toString()
//  log.info " uuid is " + uuid
           
def imageIDs = new Entity("ImageIDs")
imageIDs.shortID =  uuid
imageIDs.blobKey = blobKey.keyString
imageIDs.save()

response.status = 302

if (blobKey) {
    redirect "/success?key=${blobKey.keyString}"	
} else {
    redirect "/failure"
}
