class Top {
	bar(){}
}

class Test extends Top {
	foo() {
		return super.bar;
	}
}

