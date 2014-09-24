/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


def name='xaymaca'


println "Hello $name!"


mail.send sender: "xaymaca@gmail.com",
to: "xaymaca@gmail.com",
subject: "Hello on ${params.date}",
textBody: "content: ${params.payload}"
