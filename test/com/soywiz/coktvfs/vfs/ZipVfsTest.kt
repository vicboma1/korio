package com.soywiz.coktvfs.vfs

import com.soywiz.coktvfs.sync
import com.soywiz.coktvfs.async.toList
import com.soywiz.coktvfs.vfs.ResourcesVfs
import com.soywiz.coktvfs.vfs.openAsZip
import org.junit.Assert
import org.junit.Test

class ZipVfsTest {
    @Test
    fun testZipUncompressed() = sync {
        val helloZip = ResourcesVfs()["hello.zip"].openAsZip()

        Assert.assertEquals(
                "[VfsStat(file=ZipVfs(ResourcesVfs[hello.zip])[hello], exists=true, isDirectory=true, size=0)]",
                helloZip.list().toList().toString()
        )

        Assert.assertEquals(
                "[VfsStat(file=ZipVfs(ResourcesVfs[hello.zip])[hello/world.txt], exists=true, isDirectory=false, size=12)]",
                helloZip["hello"].list().toList().toString()
        )

        Assert.assertEquals(
                "VfsStat(file=ZipVfs(ResourcesVfs[hello.zip])[hello/world.txt], exists=true, isDirectory=false, size=12)",
                helloZip["hello/world.txt"].stat().toString()
        )
        Assert.assertEquals(
                "HELLO WORLD!",
                helloZip["hello/world.txt"].readString()
        )
    }

    @Test
    fun testZipCompressed() = sync {
        val helloZip = ResourcesVfs()["compressedHello.zip"].openAsZip()
        Assert.assertEquals(
                "HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO HELLO WORLD!",
                helloZip["hello/compressedWorld.txt"].readString()
        )
        Assert.assertEquals(
                "[hello, hello/compressedWorld.txt, hello/world.txt]",
                helloZip.listRecursive().toList().map { it.name }.toString()
        )
    }
}