def blobs = blobstore.getUploadedBlobs(request)
def blobKey = blobs["george"]

//log.info "heyyyyy! got a key " + blobKey.keyString

//response.setHeader("ourKey",blobKey.keyString)


response.status = 302

if (blobKey) {
	redirect "/respite.groovy?key=${blobKey.keyString}"	
} else {
       redirect "/failure"
}