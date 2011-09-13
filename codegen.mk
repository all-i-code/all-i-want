CODEGEN_TARGETS += \
	 test.codegen.models \
	 test.codegen.groupinvitation \
	 test.codegen.groupmember \
	 test.codegen.group \
	 test.codegen.listitem \
	 test.codegen.list \
	 test.codegen.user \
	 test.codegen.failurereport \
	 test.codegen.json.cleanup

test.codegen.json.prep:
	$(HIDE). env.sh
	$(HIDE)mkdir test.codegen.json
	$(HIDE)$(PYTHON) codegen.py -j test.codegen.json

test.codegen.groupinvitation: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupInvitation.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupInvitation.java test.codegen.json/GroupInvitation.java
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupInvitationImpl.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupInvitationImpl.java test.codegen.json/GroupInvitationImpl.java
	$(HIDE)echo "Checking eclipse/AllIWant/test/com/googlecode/alliwant/client/model/GroupInvitationTestImpl.java"
	$(HIDE)diff eclipse/AllIWant/test/com/googlecode/alliwant/client/model/GroupInvitationTestImpl.java test.codegen.json/GroupInvitationTestImpl.java

test.codegen.groupmember: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupMember.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupMember.java test.codegen.json/GroupMember.java
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupMemberImpl.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupMemberImpl.java test.codegen.json/GroupMemberImpl.java
	$(HIDE)echo "Checking eclipse/AllIWant/test/com/googlecode/alliwant/client/model/GroupMemberTestImpl.java"
	$(HIDE)diff eclipse/AllIWant/test/com/googlecode/alliwant/client/model/GroupMemberTestImpl.java test.codegen.json/GroupMemberTestImpl.java

test.codegen.group: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/Group.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/Group.java test.codegen.json/Group.java
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupImpl.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/GroupImpl.java test.codegen.json/GroupImpl.java
	$(HIDE)echo "Checking eclipse/AllIWant/test/com/googlecode/alliwant/client/model/GroupTestImpl.java"
	$(HIDE)diff eclipse/AllIWant/test/com/googlecode/alliwant/client/model/GroupTestImpl.java test.codegen.json/GroupTestImpl.java

test.codegen.listitem: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/ListItem.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/ListItem.java test.codegen.json/ListItem.java
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/ListItemImpl.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/ListItemImpl.java test.codegen.json/ListItemImpl.java
	$(HIDE)echo "Checking eclipse/AllIWant/test/com/googlecode/alliwant/client/model/ListItemTestImpl.java"
	$(HIDE)diff eclipse/AllIWant/test/com/googlecode/alliwant/client/model/ListItemTestImpl.java test.codegen.json/ListItemTestImpl.java

test.codegen.list: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/List.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/List.java test.codegen.json/List.java
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/ListImpl.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/ListImpl.java test.codegen.json/ListImpl.java
	$(HIDE)echo "Checking eclipse/AllIWant/test/com/googlecode/alliwant/client/model/ListTestImpl.java"
	$(HIDE)diff eclipse/AllIWant/test/com/googlecode/alliwant/client/model/ListTestImpl.java test.codegen.json/ListTestImpl.java

test.codegen.user: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/User.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/User.java test.codegen.json/User.java
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/UserImpl.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/UserImpl.java test.codegen.json/UserImpl.java
	$(HIDE)echo "Checking eclipse/AllIWant/test/com/googlecode/alliwant/client/model/UserTestImpl.java"
	$(HIDE)diff eclipse/AllIWant/test/com/googlecode/alliwant/client/model/UserTestImpl.java test.codegen.json/UserTestImpl.java

test.codegen.failurereport: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/FailureReport.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/FailureReport.java test.codegen.json/FailureReport.java
	$(HIDE)echo "Checking eclipse/AllIWant/src/com/googlecode/alliwant/client/model/FailureReportImpl.java"
	$(HIDE)diff eclipse/AllIWant/src/com/googlecode/alliwant/client/model/FailureReportImpl.java test.codegen.json/FailureReportImpl.java
	$(HIDE)echo "Checking eclipse/AllIWant/test/com/googlecode/alliwant/client/model/FailureReportTestImpl.java"
	$(HIDE)diff eclipse/AllIWant/test/com/googlecode/alliwant/client/model/FailureReportTestImpl.java test.codegen.json/FailureReportTestImpl.java

test.codegen.json.cleanup:
	$(HIDE)rm -rf test.codegen.json

