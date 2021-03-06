/*
 * Copyright (c) 2018 - Frank Hossfeld
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 *
 */

package com.github.nalukit.bootstarternalu.server.resource.generator.impl;

import com.github.nalukit.bootstarternalu.server.resource.generator.GeneratorConstants;
import com.github.nalukit.bootstarternalu.server.resource.generator.GeneratorUtils;
import com.github.nalukit.gwtbootstarternalu.shared.model.ControllerData;
import com.github.nalukit.gwtbootstarternalu.shared.model.GeneratorException;
import com.github.nalukit.gwtbootstarternalu.shared.model.NaluGeneraterParms;
import com.github.nalukit.nalu.client.application.IsApplication;
import com.github.nalukit.nalu.client.application.annotation.Application;
import com.github.nalukit.nalu.client.application.annotation.Debug;
import com.github.nalukit.nalu.plugin.elemental2.client.DefaultElemental2Logger;
import com.github.nalukit.nalu.plugin.gwt.client.DefaultGWTLogger;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ApplicationSourceGenerator
  extends AbstractSourceGenerator {

  private ApplicationSourceGenerator(Builder builder) {
    super();

    this.naluGeneraterParms = builder.naluGeneraterParms;
    this.directoryJava = builder.directoryJava;
    this.clientPackageJavaConform = builder.clientPackageJavaConform;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void generate()
    throws GeneratorException {

    AnnotationSpec.Builder annotation = AnnotationSpec.builder(Application.class)
                                                      .addMember("shell",
                                                                 "$T.class",
                                                                 ClassName.get(this.clientPackageJavaConform + ".ui.shell",
                                                                               "Shell"))
                                                      .addMember("context",
                                                                 GeneratorUtils.setFirstCharacterToUpperCase(this.naluGeneraterParms.getArtefactId()) + GeneratorConstants.CONTEXT + ".class")
                                                      .addMember("startRoute",
                                                                 "$S",
                                                                 "/" + getStartRoute());
    if (this.naluGeneraterParms.isApplicationLoader()) {
      annotation.addMember("loader",
                           GeneratorUtils.setFirstCharacterToUpperCase(this.naluGeneraterParms.getArtefactId()) + GeneratorConstants.LOADER + ".class");
    }

    TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder(GeneratorUtils.setFirstCharacterToUpperCase(this.naluGeneraterParms.getArtefactId()) + GeneratorConstants.APPLICAITON)
                                        .addJavadoc(CodeBlock.builder()
                                                             .add(GeneratorConstants.COPYRIGHT_JAVA)
                                                             .build())
                                        .addModifiers(Modifier.PUBLIC)
                                        .addSuperinterface(ClassName.get(IsApplication.class))
                                        .addAnnotation(annotation.build());

    // add debugging logs ????
    if (naluGeneraterParms.isDebug()) {
      typeSpec.addAnnotation(AnnotationSpec.builder(Debug.class)
                                           .addMember("logger",
                                                      "$T.class",
                                                      getLogger())
                                           .addMember("logLevel",
                                                      "$T.LogLevel.DETAILED",
                                                      ClassName.get(Debug.class))
                                           .build());
    }

    JavaFile javaFile = JavaFile.builder(this.clientPackageJavaConform,
                                         typeSpec.build())
                                .build();
    try {
      javaFile.writeTo(new File(directoryJava,
                                ""));
    } catch (IOException e) {
      throw new GeneratorException("Unable to write generated file: >>" + GeneratorUtils.setFirstCharacterToUpperCase(this.naluGeneraterParms.getArtefactId()) + GeneratorConstants.APPLICAITON + "<< -> exception: " + e.getMessage());
    }
  }

  private String getStartRoute() {
    Optional<ControllerData> optinalStartRoute = this.naluGeneraterParms.getControllers()
                                                                        .stream()
                                                                        .filter(c -> c.isShowControllerAtStart())
                                                                        .findFirst();
    if (optinalStartRoute.isPresent()) {
      return optinalStartRoute.get()
                              .getRoute();
    } else {
      return this.naluGeneraterParms.getControllers()
                                    .size() > 0 ? this.naluGeneraterParms.getControllers()
                                                                         .get(0)
                                                                         .getRoute() : "";
    }
  }

  private ClassName getLogger() {
    switch (this.naluGeneraterParms.getWidgetLibrary()) {
      case DOMINO_UI:
      case ELEMENTO:
        return ClassName.get(DefaultElemental2Logger.class);
      case GWT:
      case GXT:
        return ClassName.get(DefaultGWTLogger.class);
    }
    return null;
  }

  public static class Builder {

    NaluGeneraterParms naluGeneraterParms;
    File               directoryJava;
    String             clientPackageJavaConform;

    public Builder naluGeneraterParms(NaluGeneraterParms naluGeneraterParms) {
      this.naluGeneraterParms = naluGeneraterParms;
      return this;
    }

    public Builder directoryJava(File directoryJava) {
      this.directoryJava = directoryJava;
      return this;
    }

    public Builder clientPackageJavaConform(String clientPackageJavaConform) {
      this.clientPackageJavaConform = clientPackageJavaConform;
      return this;
    }

    public ApplicationSourceGenerator build() {
      return new ApplicationSourceGenerator(this);
    }
  }
}
