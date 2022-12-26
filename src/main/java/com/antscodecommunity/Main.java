/*
 * Copyright 2022 AntsCodeCommunity
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antscodecommunity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author antscode
 */
public class Main {
    public static void main(String[] args) {
        var directory = Path.of(System.getProperty("user.dir"), "src/main/resources/测试/");
        var zipFilePath = Path.of(directory + ".zip");
        try (var zipOutputStream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(zipFilePath)))) {
            Files.walkFileTree(directory, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (Objects.requireNonNull(dir.toFile().list()).length == 0) {
                        zipOutputStream.putNextEntry(new ZipEntry(Path.of(directory.getFileName().toString(), directory.relativize(dir).toString()) + File.separator));
                    }
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    zipOutputStream.putNextEntry(new ZipEntry(Path.of(directory.getFileName().toString(), directory.relativize(file).toString()).toString()));
                    Files.copy(file, zipOutputStream);
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
