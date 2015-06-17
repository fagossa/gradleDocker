# Integration tests

If you do integration test in your local environment you centainly been in a lot of problems installing all the needed software. Most of the times (specially on large systems) you install tools that you only needed in the test and you never use in your daily work. And even with _homebrew_ your path is going to be polluted.

## Enter Docker

As you might already know it, docker containers are great for easy setup environments. So, we're going to benefit from this fast running lighweight virtual machines in our behalf.

## How?

We are going to make _gradle task_ to start and stop any kind of docker image. For example, to start an _elasticsearch_ we could do something like this in our __build.gradle__:

```
task startES << {
    def imgName = "testElastic"
    def sout = new StringBuffer(), serr = new StringBuffer()
    def proc = "docker run -d --name ${imgName} -p 9200:9200 -p 9300:9300 elasticsearch ".execute()
    proc.consumeProcessOutput(sout, serr)
    proc.waitFor()
    println "Started image $imgName with id $sout"
}
```

There are two important things to note:
* _--name_, means that we are going to relate to our docker instance only by name
* _elasticsearch_, is the name of the image we are going to run

Obviously, we need to stop our instance. This case however is more complex as we need to define some generic benahiour for all instances. We could implement this kind of logic like this:

```
class StopDockerTask extends DefaultTask {
    String imgName = 'testElastic'
    @TaskAction
    def stopContainer() {
        def sout = new StringBuffer(), serr = new StringBuffer()
        def procPS = "docker ps -aq --filter=name=${imgName}".execute()
        procPS.consumeProcessOutput(sout, serr)
        procPS.waitFor()
        println "Stoping $imgName with id=$sout"
        if (sout) {
            def procST = "docker stop $sout".execute()
            procST.consumeProcessOutput(sout, serr)
            procST.waitFor()
            def procRM = "docker rm $sout".execute()
            procRM.consumeProcessOutput(sout, serr)
            procRM.waitFor()
        }
    }
}
```

Then, we simply override the image name specified to run the image:

```
task stopES(type: StopDockerTask) << {
  String imgName = 'testElastic'
}
```

### How do we start our docker image?

```
> gradle -q startES
```

### How do we stop our docker image?

```
> gradle -q stopES
```

### So again, what is the whole sequence?

This type this:

```
> gradle -q startES
> gradle clean test
> gradle -q stopES
```

### Automatically start our tools before our tests

Manually start our task is helpful because it avoids the need of installing everything, however, wouldn't it be nice to automagically start everything? well, actually we can; we just need to fix the dependencies of the _test_ task. Thas it!!!

```
task startMyToolTask {
 ...
}

test.dependsOn startMyToolTask
```
