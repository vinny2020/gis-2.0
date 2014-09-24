package com.xaymaca.restapi

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import org.junit.Ignore
import org.junit.Test

import static groovyx.net.http.ContentType.XML
import static groovyx.net.http.Method.GET

/**
 * Created by wonderful programmer that uses  IntelliJ IDEA.
 * User: xaymaca
 * Date: 3/16/11
 * Time: 2:01 AM
 *
 *
 */

@Ignore
public class ImageRestTest {

    String imageType = 'image/jpeg'
    String imgKey = "1001hq"




    @Test public void getServerURL_once() {

        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/simpletransform/${imgKey}/600/false/"
        println "uri is " + uri
        storedTransform(baseUrl, uri)


    }


    @Test public void getServerURL_fifty() {

        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/simpletransform/${imgKey}/600/false/"
        println "uri is " + uri
        for (i in 1..50) {
            //println i + ":"
            storedTransform(baseUrl, uri)
        }


    }



    @Test public void test_image_resize_one() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/resize/${imgKey}/300/200/"
        println "uri is " + uri

        //println i + ":"
        def resp = storedTransform(baseUrl, uri)

        //println resp.inspect()
    }

    // with a permanent id

    @Test public void test_image_resize_fifty() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/resize/${imgKey}/300/200/"
        // println "uri is " + uri
        for (i in 1..50) {
            //println i + ":"
            storedTransform(baseUrl, uri)
        }
        //println resp
    }

    @Test public void test_image_crop_one() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/crop/${imgKey}/0.1/0.1/0.9/0.9/"
        println "uri is " + uri

        //println i + ":"
        def resp = storedTransform(baseUrl, uri)

        //println resp.inspect()
    }

    @Test public void test_image_crop_fifty() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/crop/${imgKey}/0.1/0.1/0.9/0.9/"
        for (i in 1..50) {
            //println i + ":"
            storedTransform(baseUrl, uri)
        }
        //assert resp instanceof ByteArrayInputStream
    }


    @Test public void test_image_rotate_one() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/rotate/${imgKey}/270/"
        println "uri is " + uri

        //println i + ":"
        def resp = storedTransform(baseUrl, uri)

        //println resp.inspect()
    }

    @Test public void test_image_rotate_fifty() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/rotate/${imgKey}/270/"
        for (i in 1..50) {
            //println i + ":"
            storedTransform(baseUrl, uri)
        }
        //assert resp instanceof ByteArrayInputStream
    }


    @Test public void test_image_v_flip_one() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/flip/${imgKey}/vertical/"
        println "uri is " + uri

        //println i + ":"
        def resp = storedTransform(baseUrl, uri)

        // println resp.inspect()
    }

    @Test public void test_image_v_flip_fifty() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/flip/${imgKey}/vertical/"
        for (i in 1..50) {
            //println i + ":"
            storedTransform(baseUrl, uri)
        }
        //assert resp instanceof ByteArrayInputStream
    }

    @Test public void test_image_h_flip_one() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/flip/${imgKey}/horizontal/"
        println "uri is " + uri

        //println i + ":"
        def resp = storedTransform(baseUrl, uri)

        println resp.inspect()
    }

    @Test public void test_image_h_flip_fifty() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/flip/${imgKey}/horizontal/"
        for (i in 1..50) {
            //println i + ":"
            storedTransform(baseUrl, uri)
        }
        //assert resp instanceof ByteArrayInputStream
    }

    @Test public void test_image_lucky_one() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/lucky/${imgKey}/"
        println "uri is " + uri

        //println i + ":"
        def resp = storedTransform(baseUrl, uri)

        // println resp.inspect()
    }

    @Test public void test_image_lucky_fifty() {
        def baseUrl = "http://hcongruent.appspot.com"
        def uri = "/image/lucky/${imgKey}/"
        for (i in 1..50) {
            //println i + ":"
            storedTransform(baseUrl, uri)
        }
        //assert resp instanceof ByteArrayInputStream
    }








    private def storedTransform(baseUrl, myuri) {


        def http = new HTTPBuilder(baseUrl)
        def resp = null
        try {

            resp = http.request(groovyx.net.http.Method.GET, groovyx.net.http.ContentType.XML) { req ->

                uri.path = myuri
                // uri.query = [ v:'1.0', q: 'Calvin and Hobbes' ]

            }


        }
        catch (HttpResponseException ex) {
            // default failure handler throws an exception:
            println "Unexpected response error: ${ex.response.statusLine}"
        }

        return resp


    }


}