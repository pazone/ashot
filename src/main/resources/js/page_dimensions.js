var body = document.body;
var documentElement = document.documentElement;
var pageHeight = Math.max(body.scrollHeight, body.offsetHeight, documentElement.clientHeight,
    documentElement.scrollHeight, documentElement.offsetHeight);
var viewportHeight = window.innerHeight || documentElement.clientHeight|| body.clientHeight;
var viewportWidth = window.innerWidth || documentElement.clientWidth || body.clientWidth;
return {"pageHeight": pageHeight, "viewportHeight": viewportHeight, "viewportWidth": viewportWidth}