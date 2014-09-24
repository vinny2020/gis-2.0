package com.xaymaca.restapi

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.junit.Test

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.POST

/**
 * Created by wonderful programmer that uses  IntelliJ IDEA.
 * User: xaymaca
 * Date: 3/16/11
 * Time: 2:01 AM
 *
 *
 */


public class StreamRestTest {

    String imageType = 'image/jpeg'
    File imgFile = new File('/Users/vstoessel/Pictures/DSC_2802.jpg')
    FileBody cbFile = new FileBody(imgFile, imageType)
    StringBody oneParam = new StringBody("Submit")



    @Test public void test_stream_resize_one() {
        def baseUrl = "http://hcongruent.appspot.com/stream/resize/300/200/"

        streamTransform(baseUrl)

    }




    @Test public void test_stream_resize_50() {
        def baseUrl = "http://hcongruent.appspot.com/stream/resize/300/200/"
        for (i in 1..50) {
            //println i + ":"
            streamTransform(baseUrl)
        }
        //assert resp instanceof ByteArrayInputStream

    }


        @Test public void test_stream_crop_one() {
        def baseUrl = "http://hcongruent.appspot.com/stream/crop/0.1/0.1/0.9/0.9/"

        streamTransform(baseUrl)

    }

    @Test public void test_stream_crop_50() {
        def baseUrl = "http://hcongruent.appspot.com/stream/crop/0.1/0.1/0.9/0.9/"
        for (i in 1..50) {
            //println i + ":"
            streamTransform(baseUrl)
        }
        //assert resp instanceof ByteArrayInputStream
    }

        @Test public void test_stream_rotate_one() {
        def baseUrl = "http://hcongruent.appspot.com/stream/rotate/270/"

        streamTransform(baseUrl)

    }

    @Test public void test_stream_rotate_50() {
        def baseUrl = "http://hcongruent.appspot.com/stream/rotate/270/"
        for (i in 1..50) {
            //println i + ":"
            streamTransform(baseUrl)
        }
        //assert resp instanceof ByteArrayInputStream
    }


        @Test public void test_stream_vertical_flip_one() {
        def baseUrl = "http://hcongruent.appspot.com/stream/verticalflip/"

        streamTransform(baseUrl)

    }

    @Test public void test_stream_vertical_flip_50() {
        def baseUrl = "http://hcongruent.appspot.com/stream/verticalflip/"
        for (i in 1..50) {
            //println i + ":"
            streamTransform(baseUrl)
        }
        //assert resp instanceof ByteArrayInputStream
    }

        @Test public void test_stream_horizontal_flip_one() {
        def baseUrl = "http://hcongruent.appspot.com/stream/horizontalflip/"

        streamTransform(baseUrl)

    }

    @Test public void test_stream_horizontal_flip_50() {
        def baseUrl = "http://hcongruent.appspot.com/stream/horizontalflip/"
        for (i in 1..50) {
            //println i + ":"
            streamTransform(baseUrl)
        }
        //assert resp instanceof ByteArrayInputStream
    }

        @Test public void test_stream_lucky_one() {
        def baseUrl = "http://hcongruent.appspot.com/stream/lucky/"

        streamTransform(baseUrl)

    }

    @Test public void test_stream_lucky_50() {
        def baseUrl = "http://hcongruent.appspot.com/stream/lucky/"
        for (i in 1..50) {
            //println i + ":"
            streamTransform(baseUrl)
        }
        //assert resp instanceof ByteArrayInputStream
    }




    private def streamTransform(baseUrl) {


        def http = new HTTPBuilder(baseUrl)
        def resp = null
        try {

            resp = http.request(groovyx.net.http.Method.POST) { req ->

                def mpEntity = new MultipartEntity()
                mpEntity.addPart("photo", cbFile)
                mpEntity.addPart("submit", oneParam)
                // mpEntity.addPart("type", type)

                req.entity = mpEntity


            }


        }
        catch (HttpResponseException ex) {
            // default failure handler throws an exception:
            println "Unexpected response error: ${ex.response.contentType}"
        }

        return resp


    }


}