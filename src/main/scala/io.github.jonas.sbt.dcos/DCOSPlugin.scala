/*
 * Copyright 2017 Jonas Fonseca
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

package io.github.jonas.sbt.dcos

import sbt._
import sbt.Keys._
import java.nio.file.Files
import java.nio.file.attribute.{ PosixFileAttributeView, PosixFilePermission }

/**
 * Provides a way to manage and run the DC/OS CLI from sbt.
 */
object DCOSPlugin extends AutoPlugin {

  object autoImport {
    val DCOS = config("dcos")
    val dcosCli = taskKey[File]("Path to the DC/OS CLI")
  }
  import autoImport._

  val arch = sys.props("os.name").toLowerCase.take(3) match {
    case "win" => "windows"
    case "mac" => "darwin"
    case _ => "linux"
  }

  val dcosCliName = s"dcos-cli-$arch"

  override def requires = plugins.JvmPlugin

  override def projectSettings: Seq[Setting[_]] = Seq(
    version in dcosCli := "0.4.16",
    ivyConfigurations += DCOS,
    libraryDependencies += {
      val dcosCliVersion = (version in dcosCli).value
      val dcosCliUrl = s"https://downloads.dcos.io/binaries/cli/$arch/x86-64/$dcosCliVersion/dcos"
      "io.github.jonas" % dcosCliName % dcosCliVersion % DCOS from(dcosCliUrl)
    },
    dcosCli := {
      val cli = (update in DCOS).value.select(artifact = artifactFilter(name = dcosCliName)).head

      // Set the executable bit on the expected path to fail if it doesn't exist
      for (view <- Option(Files.getFileAttributeView(cli.toPath, classOf[PosixFileAttributeView]))) {
        val permissions = view.readAttributes.permissions
        if (permissions.add(PosixFilePermission.OWNER_EXECUTE))
          view.setPermissions(permissions)
      }

      cli
    }
  )

}
