FROM jenkins/jenkins
MAINTAINER Android

ARG ANDROID_SDK_TOOLS_ZIP=sdk-tools-linux-4333796.zip
ARG GRADLE_VERSION=5.1.1

ENV GRADLE_NAME gradle-$GRADLE_VERSION
ENV GRADLE_ZIP gradle-$GRADLE_VERSION-bin.zip
ENV ANDROID_HOME /opt/android/sdk
ENV PATH $ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$ANDROID_HOME/$GRADLE_NAME/bin:$PATH

# https://developer.android.com/studio/#command-tools
ENV ANDROID_SDK_TOOLS_URL https://dl.google.com/android/repository/$ANDROID_SDK_TOOLS_ZIP
ENV GRADLE_URL https://services.gradle.org/distributions/$GRADLE_ZIP

USER root

RUN apt-get -y update && apt-get -y install vim

# Download Gradle and Android SDK Tools
RUN wget -P /opt/ $GRADLE_URL \
	&& wget -P /opt/ $ANDROID_SDK_TOOLS_URL

# Install Gradle and Android SDK Tools
RUN mkdir -p $ANDROID_HOME \
	&& unzip /opt/$ANDROID_SDK_TOOLS_ZIP -d $ANDROID_HOME/ \
	&& unzip /opt/$GRADLE_ZIP -d $ANDROID_HOME/ \
	&& rm /opt/$ANDROID_SDK_TOOLS_ZIP \
	&& rm /opt/$GRADLE_ZIP
	
#Set sdk licenses, install platform-tools and required sdk, build-tools
RUN yes | $ANDROID_HOME/tools/bin/sdkmanager --licenses \
	&& yes | android update sdk --no-ui --all --filter platform-tools,android-29,build-tools-29.0.0

USER jenkins
# List desired Jenkins plugins here
RUN /usr/local/bin/install-plugins.sh workflow-aggregator git android-lint build-monitor-plugin
