Array.prototype.pushIf = function() {
	for(var i = 0, length = arguments.length; i < length; i++) {
		var element = arguments[i];
		if(this.indexOf(element) == -1)
			this.push(element);
	}
	return this;
};

Array.prototype.insert = function(element, index) {
	element = Array.isArray(element) ? element : [element];
	var args = [index != null ? index : this.length, 0].concat(element);
	this.splice.apply(this, args);
	return this;
};

Array.prototype.add = function(element) {
	return this.insert(element, null);
};

Array.prototype.remove = function() {
	var removed = [];
	for(var i = 0, length = arguments.length; i < length; i++) {
		var element = arguments[i];
		var index = this.indexOf(element);
		if(this.indexOf(element) != -1) {
			removed.push(element);
			this.splice(index, 1);
		}
	}
	return removed;
};

Array.prototype.removeAll = function(array) {
	if(array == null)
		return [];

	var array = Array.isArray(array) ? array : [array];
	return this.remove.apply(this, array);
};

Array.prototype.removeAt = function(index) {
	return this.splice(index, 1);
};

Array.prototype.first = function() {
	var length = this.length;
	return length > 0 ? this[0] : null;
};

Array.prototype.last = function() {
	var length = this.length;
	return length > 0 ? this[length - 1] : null;
};