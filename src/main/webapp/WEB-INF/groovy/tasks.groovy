/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

def content = "a rose by any other name"


// add a task to the queue
def queue = queues.default

for (i in 1..7) {

queue.add countdownMillis: 20000, url: "/mailme.groovy",
method: 'PUT', params: [date: "20090914-${i}" ],
payload: content
}

