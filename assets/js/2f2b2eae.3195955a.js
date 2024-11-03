"use strict";(self.webpackChunkmomosdk=self.webpackChunkmomosdk||[]).push([[906],{1295:(e,n,i)=>{i.r(n),i.d(n,{assets:()=>a,contentTitle:()=>o,default:()=>c,frontMatter:()=>t,metadata:()=>l,toc:()=>h});var s=i(4848),r=i(8453);const t={sidebar_position:2,sidebar_label:"SDK Publishing"},o="SDK Publishing",l={id:"engineering/getting-started/publishing-sdk",title:"SDK Publishing",description:"Publishing the MTN MOMO API SDK is a crucial step in making it available for developers to integrate into their applications. We utilize the Gradle Maven Publish Plugin to facilitate the publishing process. This plugin simplifies the configuration and management of publishing artifacts to Maven repositories, ensuring a smooth and efficient workflow.",source:"@site/versioned_docs/version-1.0/engineering/getting-started/publishing-sdk.md",sourceDirName:"engineering/getting-started",slug:"/engineering/getting-started/publishing-sdk",permalink:"/engineering/getting-started/publishing-sdk",draft:!1,unlisted:!1,editUrl:"https://github.com/re-kast/android-mtn-momo-api-sdk/tree/develop/versioned_docs/version-1.0/engineering/getting-started/publishing-sdk.md",tags:[],version:"1.0",sidebarPosition:2,frontMatter:{sidebar_position:2,sidebar_label:"SDK Publishing"},sidebar:"tutorialSidebar",previous:{title:"Developer Setup",permalink:"/engineering/getting-started/developer-setup"},next:{title:"Code Testing",permalink:"/engineering/getting-started/testing"}},a={},h=[{value:"Publishing to Remote Repositories",id:"publishing-to-remote-repositories",level:2},{value:"Steps for Publishing to Remote Repositories",id:"steps-for-publishing-to-remote-repositories",level:3},{value:"Publishing Command",id:"publishing-command",level:3},{value:"Publishing Locally",id:"publishing-locally",level:2},{value:"Additional Information",id:"additional-information",level:2},{value:"References",id:"references",level:2}];function d(e){const n={a:"a",admonition:"admonition",code:"code",h1:"h1",h2:"h2",h3:"h3",header:"header",hr:"hr",li:"li",ol:"ol",p:"p",pre:"pre",strong:"strong",ul:"ul",...(0,r.R)(),...e.components};return(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(n.header,{children:(0,s.jsx)(n.h1,{id:"sdk-publishing",children:"SDK Publishing"})}),"\n",(0,s.jsxs)(n.p,{children:["Publishing the MTN MOMO API SDK is a crucial step in making it available for developers to integrate into their applications. We utilize the ",(0,s.jsx)(n.a,{href:"https://vanniktech.github.io/gradle-maven-publish-plugin/",children:"Gradle Maven Publish Plugin"})," to facilitate the publishing process. This plugin simplifies the configuration and management of publishing artifacts to Maven repositories, ensuring a smooth and efficient workflow."]}),"\n",(0,s.jsx)(n.hr,{}),"\n",(0,s.jsx)(n.h2,{id:"publishing-to-remote-repositories",children:"Publishing to Remote Repositories"}),"\n",(0,s.jsx)(n.p,{children:"We currently publish the SDK to two primary repositories:"}),"\n",(0,s.jsxs)(n.ol,{children:["\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Sonatype OSS Repository"}),":"]}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Snapshots Repository"}),": This repository is used for publishing development versions of the SDK, allowing developers to test new features before they are officially released.","\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:["URL: ",(0,s.jsx)(n.a,{href:"https://s01.oss.sonatype.org/content/repositories/snapshots/io/rekast/momo-api-sdk/",children:"Snapshots"})]}),"\n"]}),"\n"]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:"Releases Repository"}),": This repository is used for stable, production-ready versions of the SDK, ensuring that developers have access to reliable and tested versions.","\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:["URL: ",(0,s.jsx)(n.a,{href:"https://s01.oss.sonatype.org/content/repositories/releases/io/rekast/momo-api-sdk/",children:"Releases"})]}),"\n"]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Maven Central"}),":"]}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:["This repository is the primary public repository for Java libraries. It is widely used and trusted by developers. Note that it does not accept ",(0,s.jsx)(n.code,{children:"SNAPSHOT"})," versions, so ensure that you are publishing stable releases.","\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:["URL: ",(0,s.jsx)(n.a,{href:"https://repo1.maven.org/maven2/io/rekast/momo-api-sdk/",children:"Maven Central"})]}),"\n"]}),"\n"]}),"\n"]}),"\n"]}),"\n"]}),"\n",(0,s.jsx)(n.h3,{id:"steps-for-publishing-to-remote-repositories",children:"Steps for Publishing to Remote Repositories"}),"\n",(0,s.jsx)(n.p,{children:"When publishing to remote repositories, remember to follow these steps:"}),"\n",(0,s.jsxs)(n.ol,{children:["\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Update the SDK Version"}),":"]}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:["Update the ",(0,s.jsx)(n.a,{href:"https://github.com/re-kast/android-mtn-momo-api-sdk/blob/9d70fb3cf954b8626f0facb67d5e00d0652a9305/android/momo-api-sdk/gradle.properties#L4",children:"version"})," of the SDK in the ",(0,s.jsx)(n.code,{children:"gradle.properties"})," file. This ensures that the correct version is published."]}),"\n"]}),"\n",(0,s.jsx)(n.admonition,{type:"info",children:(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-properties",children:"VERSION_NAME=0.0.2-SNAPSHOT\n"})})}),"\n"]}),"\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Select the Publishing Server"}),":"]}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:["Choose the server to publish to by uncommenting either of the following ",(0,s.jsx)(n.a,{href:"https://github.com/re-kast/android-mtn-momo-api-sdk/blob/de966147f9a240b2bdf0611663a6fd5b05cf21ae/android/momo-api-sdk/gradle.properties#L24-L25",children:"lines"}),":"]}),"\n"]}),"\n",(0,s.jsx)(n.admonition,{type:"info",children:(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-properties",children:"#SONATYPE_HOST=S01\n#SONATYPE_HOST=CENTRAL_PORTAL\n"})})}),"\n"]}),"\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:["Update your global ",(0,s.jsx)(n.code,{children:"gradle.properties"})," found on ",(0,s.jsx)(n.code,{children:"~/.gradle/gradle.properties"})," with the correct authentication credentials."]}),"\n",(0,s.jsx)(n.admonition,{type:"warning",children:(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Important Reminder"}),": Both Sonatype OSS and Maven Central require tokens for authentication, so log in to the portals and generate the necessary tokens."]})}),"\n"]}),"\n"]}),"\n",(0,s.jsx)(n.h3,{id:"publishing-command",children:"Publishing Command"}),"\n",(0,s.jsx)(n.p,{children:"To publish and release the SDK to Maven Central, run the following command in your terminal:"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-bash",children:"./gradlew publishAndReleaseToMavenCentral --no-configuration-cache --stacktrace\n"})}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:(0,s.jsx)(n.code,{children:"--no-configuration-cache"})}),": This flag disables the configuration cache, which can help avoid issues during the publishing process, especially if there are changes in the build configuration."]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.strong,{children:(0,s.jsx)(n.code,{children:"--stacktrace"})}),": This flag provides a detailed stack trace in case of errors, which can be useful for debugging and identifying issues during the publishing process."]}),"\n"]}),"\n",(0,s.jsx)(n.h2,{id:"publishing-locally",children:"Publishing Locally"}),"\n",(0,s.jsx)(n.p,{children:"For testing changes locally before publishing to remote repositories, you can publish the SDK artifact to your local Maven repository. This is particularly useful for verifying changes without affecting the remote repositories."}),"\n",(0,s.jsx)(n.p,{children:"To publish an artifact locally, run the following command:"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-bash",children:"./gradlew publishToMavenLocal --no-configuration-cache\n"})}),"\n",(0,s.jsxs)(n.p,{children:["This command will place the published artifact in your local Maven repository, typically located at ",(0,s.jsx)(n.code,{children:"~/.m2/repository/"}),", allowing you to test the SDK in your local projects."]}),"\n",(0,s.jsx)(n.h2,{id:"additional-information",children:"Additional Information"}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Versioning"}),": Ensure that you follow ",(0,s.jsx)(n.a,{href:"https://semver.org/",children:"semantic versioning"})," practices when publishing releases. This helps users understand the nature of changes in each version (e.g., major, minor, patch). For example, increment the major version for breaking changes, the minor version for new features, and the patch version for bug fixes."]}),"\n"]}),"\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Documentation"}),": It is essential to maintain up-to-date documentation for your SDK. Consider using tools like ",(0,s.jsx)(n.a,{href:"https://kotlinlang.org/docs/dokka/overview.html",children:"Dokka"})," to generate documentation from your Kotlin code. Well-documented SDKs improve usability and help developers integrate your library more effectively."]}),"\n"]}),"\n",(0,s.jsxs)(n.li,{children:["\n",(0,s.jsxs)(n.p,{children:[(0,s.jsx)(n.strong,{children:"Changelog"}),": Maintain a changelog to document changes between versions. This can be a simple markdown file that outlines new features, bug fixes, and any breaking changes. This practice enhances transparency and helps users understand what to expect in each release."]}),"\n"]}),"\n"]}),"\n",(0,s.jsx)(n.h2,{id:"references",children:"References"}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsx)(n.li,{children:(0,s.jsx)(n.a,{href:"https://vanniktech.github.io/gradle-maven-publish-plugin/",children:"Gradle Maven Publish Plugin Documentation"})}),"\n",(0,s.jsx)(n.li,{children:(0,s.jsx)(n.a,{href:"https://central.sonatype.com/",children:"Sonatype OSS Repository Hosting"})}),"\n",(0,s.jsx)(n.li,{children:(0,s.jsx)(n.a,{href:"https://repo1.maven.org/maven2/",children:"Maven Central Repository"})}),"\n",(0,s.jsx)(n.li,{children:(0,s.jsx)(n.a,{href:"https://semver.org/",children:"Semantic Versioning"})}),"\n",(0,s.jsx)(n.li,{children:(0,s.jsx)(n.a,{href:"https://kotlinlang.org/docs/dokka/overview.html",children:"Dokka Documentation"})}),"\n",(0,s.jsx)(n.li,{children:(0,s.jsx)(n.a,{href:"https://keepachangelog.com/en/1.0.0/",children:"Creating a Changelog"})}),"\n",(0,s.jsx)(n.li,{children:(0,s.jsx)(n.a,{href:"https://www.baeldung.com/publishing-java-libraries",children:"Best Practices for Publishing Java Libraries"})}),"\n"]}),"\n",(0,s.jsx)(n.p,{children:"By following these guidelines, you can effectively publish the MTN MOMO API SDK, ensuring that it is accessible and usable for developers looking to integrate mobile money services into their applications."})]})}function c(e={}){const{wrapper:n}={...(0,r.R)(),...e.components};return n?(0,s.jsx)(n,{...e,children:(0,s.jsx)(d,{...e})}):d(e)}},8453:(e,n,i)=>{i.d(n,{R:()=>o,x:()=>l});var s=i(6540);const r={},t=s.createContext(r);function o(e){const n=s.useContext(t);return s.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function l(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(r):e.components||r:o(e.components),s.createElement(t.Provider,{value:n},e.children)}}}]);