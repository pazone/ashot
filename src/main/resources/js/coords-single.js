function Coords(el) {
    this.left = parseInt(el.offset().left);
    this.top = parseInt(el.offset().top);
    this.right = parseInt(this.left + el.outerWidth());
    this.bottom = parseInt(this.top + el.outerHeight());
}

Coords.prototype.toString = function () {
    var x = Math.max(this.left, 0);
    var y = Math.max(this.top, 0);
    return JSON.stringify({
        x:x,
        y:y,
        width:this.right - x,
        height:this.bottom - y
    });
};

return [(new Coords($(arguments[0]))).toString()];
