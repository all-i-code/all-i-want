CODEGEN_TARGETS += \
	 test.codegen.models \
	 test.codegen.account \
	 test.codegen.category \
	 test.codegen.split \
	 test.codegen.transaction \
	 test.codegen.paymenttype \
	 test.codegen.bill \
	 test.codegen.budget \
	 test.codegen.budgetitem \
	 test.codegen.data \
	 test.codegen.user \
	 test.codegen.failurereport \
	 test.codegen.json.cleanup

test.codegen.models:
	$(HIDE)echo "Checking models.py"
	$(HIDE). env.sh
	$(HIDE)$(PYTHON) codegen.py -m $@.out
	$(HIDE)diff models.py $@.out
	$(HIDE)rm -f $@.out

test.codegen.json.prep:
	$(HIDE). env.sh
	$(HIDE)mkdir test.codegen.json
	$(HIDE)$(PYTHON) codegen.py -j test.codegen.json

test.codegen.account: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Account.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Account.java test.codegen.json/Account.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/AccountImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/AccountImpl.java test.codegen.json/AccountImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/AccountTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/AccountTestImpl.java test.codegen.json/AccountTestImpl.java

test.codegen.category: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Category.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Category.java test.codegen.json/Category.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/CategoryImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/CategoryImpl.java test.codegen.json/CategoryImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/CategoryTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/CategoryTestImpl.java test.codegen.json/CategoryTestImpl.java

test.codegen.split: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Split.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Split.java test.codegen.json/Split.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/SplitImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/SplitImpl.java test.codegen.json/SplitImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/SplitTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/SplitTestImpl.java test.codegen.json/SplitTestImpl.java

test.codegen.transaction: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Transaction.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Transaction.java test.codegen.json/Transaction.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/TransactionImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/TransactionImpl.java test.codegen.json/TransactionImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/TransactionTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/TransactionTestImpl.java test.codegen.json/TransactionTestImpl.java

test.codegen.paymenttype: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/PaymentType.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/PaymentType.java test.codegen.json/PaymentType.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/PaymentTypeImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/PaymentTypeImpl.java test.codegen.json/PaymentTypeImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/PaymentTypeTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/PaymentTypeTestImpl.java test.codegen.json/PaymentTypeTestImpl.java

test.codegen.bill: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Bill.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Bill.java test.codegen.json/Bill.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/BillImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/BillImpl.java test.codegen.json/BillImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/BillTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/BillTestImpl.java test.codegen.json/BillTestImpl.java

test.codegen.budget: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Budget.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Budget.java test.codegen.json/Budget.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/BudgetImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/BudgetImpl.java test.codegen.json/BudgetImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/BudgetTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/BudgetTestImpl.java test.codegen.json/BudgetTestImpl.java

test.codegen.budgetitem: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/BudgetItem.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/BudgetItem.java test.codegen.json/BudgetItem.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/BudgetItemImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/BudgetItemImpl.java test.codegen.json/BudgetItemImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/BudgetItemTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/BudgetItemTestImpl.java test.codegen.json/BudgetItemTestImpl.java

test.codegen.data: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Data.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/Data.java test.codegen.json/Data.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/DataImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/DataImpl.java test.codegen.json/DataImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/DataTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/DataTestImpl.java test.codegen.json/DataTestImpl.java

test.codegen.user: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/User.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/User.java test.codegen.json/User.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/UserImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/UserImpl.java test.codegen.json/UserImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/UserTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/UserTestImpl.java test.codegen.json/UserTestImpl.java

test.codegen.failurereport: test.codegen.json.prep
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/FailureReport.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/FailureReport.java test.codegen.json/FailureReport.java
	$(HIDE)echo "Checking eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/FailureReportImpl.java"
	$(HIDE)diff eclipse/JhbGwt/src/com/googlecode/jhb/gwt/client/model/FailureReportImpl.java test.codegen.json/FailureReportImpl.java
	$(HIDE)echo "Checking eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/FailureReportTestImpl.java"
	$(HIDE)diff eclipse/JhbGwt/test/com/googlecode/jhb/gwt/client/model/FailureReportTestImpl.java test.codegen.json/FailureReportTestImpl.java

test.codegen.json.cleanup:
	$(HIDE)rm -rf test.codegen.json

