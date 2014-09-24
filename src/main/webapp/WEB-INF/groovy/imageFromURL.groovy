
/**
 * Created by a wonderful Programmer known as:
 * Vincent Stoessel
 * vstoessel@huronconsultinggroup.com
 * on 1/31/11 at  2:59 AM
 * 
 */

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
import com.xaymaca.appengine.BlobCreator
import org.apache.commons.io.IOUtils




def imgurl = params.imgurl



log.info "final url " + imgurl
def url = new URL(imgurl)
InputStream is = url.openStream()

byte[] imageArray = IOUtils.toByteArray(is)
def image = imageArray.image
def memkey = request.getQueryString()

//response.setHeader("Content-Disposition","attachment;filename=${imageKey}.${imgType.split("/")[1] }")

String imgType = image.getFormat()
def mime = "image/" + imgType.toLowerCase()


def creator = new BlobCreator()

def keymap = creator.addBlob(memkey, "george", imageArray, mime)
if (keymap != null && keymap.get("blobKey") != null) {
  // log.info "we are good.  " +  " new key " + newKey
  if (keymap.get("cached") == false) {
    Persister p = new Persister(keymap.get("blobKey"))
    uuid = p.persist()
  }
  else {
    def idx = new IdExchange()
    uuid = idx.blobToId(keymap.get("blobKey"))
  }

  // set response type
  response.setContentType("text/xml")



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
  StringBuilder errorXml = new StringBuilder("<?xml version=\" 1.0 \" encoding=\" UTF - 8 \"?>");
  errorXml.append("<errors>\n");
  errorXml.append("<error type=\"timeout\" message=\"Timed Out, URLFetch exceeded 10 seconds\" />\n");
  errorXml.append("</errors>\n");

  out << errorXml.toString()

}



