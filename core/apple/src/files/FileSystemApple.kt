/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */
@file:OptIn(ExperimentalForeignApi::class)

package kotlinx.io.files

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import kotlinx.io.IOException
import platform.Foundation.NSTemporaryDirectory
import platform.posix.*


internal actual fun atomicMoveImpl(source: Path, destination: Path) {
    if (rename(source.path, destination.path) != 0) {
        throw IOException("Move failed: ${strerror(errno)?.toKString()}")
    }
}

public actual val SystemTemporaryDirectory: Path
    get() = Path(NSTemporaryDirectory())

internal actual fun dirnameImpl(path: String): String {
    memScoped {
        return dirname(path.cstr.ptr)?.toKString() ?: ""
    }
}

internal actual fun basenameImpl(path: String): String {
    memScoped {
        return basename(path.cstr.ptr)?.toKString() ?: ""
    }
}

internal actual fun isAbsoluteImpl(path: String): Boolean = path.startsWith('/')

internal actual fun mkdirImpl(path: String) {
    if (mkdir(path, PermissionAllowAll) != 0) {
        throw IOException("mkdir failed: ${strerror(errno)?.toKString()}")
    }
}
