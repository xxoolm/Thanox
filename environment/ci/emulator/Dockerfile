# Android development environment for ubuntu.
# version 0.0.5

FROM ubuntu

# https://github.com/tracer0tong/android-emulator
MAINTAINER Android

# Specially for SSH access and port redirection
ENV ROOTPASSWORD android

# Expose ADB, ADB control and VNC ports
EXPOSE 22
EXPOSE 5037
EXPOSE 5554
EXPOSE 5555
EXPOSE 5900
EXPOSE 80
EXPOSE 443

ENV DEBIAN_FRONTEND noninteractive
RUN echo "debconf shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections && \
    echo "debconf shared/accepted-oracle-license-v1-1 seen true" | debconf-set-selections

USER root

# Update packages
RUN apt-get -y update && \
    apt-get -y install software-properties-common bzip2 unzip ssh net-tools openssh-server socat curl && \
    add-apt-repository ppa:openjdk-r/ppa && \
    apt-get update && \
    apt-get -y install openjdk-8-jdk && \
    rm -rf /var/lib/apt/lists/*

ARG ANDROID_SDK_TOOLS_ZIP=sdk-tools-linux-4333796.zip
ARG GRADLE_VERSION=5.1.1

ENV GRADLE_NAME gradle-$GRADLE_VERSION
ENV GRADLE_ZIP gradle-$GRADLE_VERSION-bin.zip
ENV ANDROID_HOME /opt/android/sdk
ENV PATH $ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$ANDROID_HOME/$GRADLE_NAME/bin:$PATH

# https://developer.android.com/studio/#command-tools
ENV ANDROID_SDK_TOOLS_URL https://dl.google.com/android/repository/$ANDROID_SDK_TOOLS_ZIP
ENV GRADLE_URL https://services.gradle.org/distributions/$GRADLE_ZIP

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

# Install latest android tools and system images
RUN ${ANDROID_HOME}/tools/bin/sdkmanager --install  "system-images;android-28;default;x86"

# Run sshd
RUN mkdir /var/run/sshd && \
    echo "root:$ROOTPASSWORD" | chpasswd && \
    sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd && \
    echo "export VISIBLE=now" >> /etc/profile

ENV NOTVISIBLE "in users profile"

# Add entrypoint
ADD entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
