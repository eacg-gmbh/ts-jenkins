function allowBreakBuild(component){
	var rootElement = component;
	while(!elementHasClass(rootElement, 'ecspublisher-break-build')){
		rootElement = rootElement.parentElement;
	}
	var fieldset = rootElement.nextSibling.nextSibling.querySelector('fieldset');
	fieldset.disabled = !component.checked;
	component.checked ? removeClass(fieldset, 'hidden') : addClass(fieldset, 'hidden');
}

function allowBreakBuildField(component){
	var rootElement = component;
	while(!elementHasClass(rootElement, 'ecspublisher-break-build-check')){
		rootElement = rootElement.parentElement;
	}
	var fieldset = rootElement.querySelector('fieldset');
	fieldset.disabled = !component.checked;
	component.checked ? removeClass(fieldset, 'hidden') : addClass(fieldset, 'hidden');
}

function addClass(element, clazz) {
	if (!elementHasClass(element, clazz)) {
		element.className += ' ' + clazz;
	}
}

function removeClass(element, clazz) {
	if (elementHasClass(element, clazz)) {
		element.className = element.className.replace(new RegExp("( |^)" + clazz + "( |$)", 'g'), ' ');
	}
}

function elementHasClass(element, clazz) {
	return new RegExp("( |^)" + clazz + "( |$)").test(element.className);
}
