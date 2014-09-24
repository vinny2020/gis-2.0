/**
 * Created by a wonderful Programmer known as:
 * Vincent Stoessel
 * vstoessel@huronconsultinggroup.com
 * on 2/6/11 at  8:01 PM
 *
 */

import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.blobstore.BlobstoreService
import com.google.appengine.api.blobstore.BlobstoreServiceFactory
import groovy.util.XmlSlurper
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.images.CompositeTransform;
import com.xaymaca.appengine.BlobCreator
import com.xaymaca.appengine.IdExchange
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.images.ImagesService.OutputEncoding
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.PreparedQuery

//log.info   "our xml in here? "   + request.getAttributeNames().dump()

//def xml = request.getInputStream().getText()

//log.info "the grail? "  + xml


//TODO Form must have all params

def outType


if(params.stream=="true") {
  outType = "stream"
}

log.info "params are "  + params

def op = params.op as String

//def transformations = new XmlSlurper().parseText(xml)
//def allTransformations = transformations.transform
//log.info " this many " + allTransformations.size()
//def imageKey = transformations.image.@key.toString()
def imageKey = params.imgKey as String
String encodeString = params.encodeAs as String
Enum encodeAs

if (encodeString != null && encodeString != "") {
    if (encodeString.toUpperCase() == "PNG") encodeAs = OutputEncoding.PNG
    if (encodeString.toUpperCase() == "JPEG") encodeAs = OutputEncoding.JPEG
    // log.info "encodeAs is a " + encodeAs.getClass()
}


def idx = new IdExchange()
def blobKeyString = idx.idToBlob(imageKey)

def memkey = request.getQueryString()

def blob = new BlobKey(blobKeyString)
def image = images.makeImageFromBlob(blob)



switch (op) {

    case "resize":
        //log.info "resize with params: " + change.scale.@height + " and " + change.scale.@width
        def x = new Integer(params.xaxis)
        def y = new Integer(params.yaxis)
        //cp.concatenate(ImagesServiceFactory.makeResize(x, y))
        image.resize(x, y)
        //log.info "scaling with resize(${x},${y})"
        break

    case "crop":
        //log.info "crop with params " + change.position.@leftX + " " + change.position.@topY + " " + change.position.@rightX + " " + change.position.@bottomY
        Double leftX = Double.valueOf(params.leftX as String)
        Double topY = Double.valueOf(params.topY as String)
        Double rightX = Double.valueOf(params.rightX as String)
        Double bottomY = Double.valueOf(params.bottomY as String)
        image.crop(leftX, topY, rightX, bottomY)

        //cp.concatenate(ImagesServiceFactory.makeCrop())
        //log.info "crop with leftX:  ${leftX} topY: ${topY} rightX: ${rightX} bottomY: ${bottomY}"
        break

    case "rotate":
        //log.info "rotate with params: " + params.degrees
        image.rotate(params.degrees as Integer)
        //cp.concatenate(ImagesServiceFactory.makeRotate(Integer.valueOf(change.angle.@degrees as String)))
        break

    case "flip":
        //log.info "flip with  " + params.orientation
        if (params.orientation == "vertical") image.verticalFlip()
        if (params.orientation == "horizontal") image.horizontalFlip()
        break

    case "lucky":
        //log.info "lucky"
        image.imFeelingLucky()
        break

    default:
        log.info "Don't know"
}

def letters = "a".."z";
def random = new Random()

def random1 = random.nextInt(letters.size())
def random2 = random.nextInt(letters.size())
def letterz = letters.get(random1) + letters.get(random2);

byte[] binImage = image.imageData
String imgType = image.getFormat()
def uuid

if(outType != "stream") {


def mime = "image/" + imgType.toLowerCase()

def creator = new BlobCreator()
def keymap = creator.addBlob(memkey, "george", binImage, mime)



if (keymap != null && keymap.get("blobKey") != null) {
    //log.info "we are good.  " + "oldkey " + blobKeyString + " new key " + newKey

    if (keymap.get("cached") == false) {

        log.info " blobkey is " + keymap.get("blobKey")

        def imageIDs = new Entity("ImageIDs")




        imageIDs.blobKey = keymap.get("blobKey")
        imageIDs.save()
        uuid = imageIDs.getKey().getId() + letterz
        log.info "our new uuid is " + uuid
        imageIDs.shortID = uuid
        imageIDs.save()


        log.info "this is our ID entity " + uuid


    }
    else {

        uuid = idx.blobToId(keymap.get("blobKey"))
    }

    log.info "xml outType was " + outType
    outType  =   "xml"
}

    else {

    // set response type
    log.info "before error, outType was " + outType
    outType = "error"

}


}



// split output types




switch (outType) {

    case "error":
        response.setContentType("text/xml")
        response.status = 500
        StringBuilder errorXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        errorXml.append("<errors>\n");
        errorXml.append("<error type=\"timeout\"  message=\"Timed Out, URLFetch exceeded 10 seconds\" />\n");
        errorXml.append("</errors>\n");
        out << errorXml.toString()
        break

    case "stream":
      def tmpName = "img-" + letterz

        response.setContentType("application/octet-stream")
    response.setHeader("Content-Disposition","attachment;filename=${tmpName}.${imgType.toLowerCase()}")
        log.info "sending binary version"
        out << binImage
        break


    case "xml":
        response.setContentType("text/xml")
    def responseXML = """<?xml version="1.0" encoding="UTF-8" ?>
<image>
<key>${uuid}</key>
<fileURL>/image/${uuid}/</fileURL>
</image>
"""
           out << responseXML
    break

    default:
    log.info "no output specified"
}