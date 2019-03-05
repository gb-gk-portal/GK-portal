function constructFlatObject(flat) {
    const fNumber = flat.children('.gk-flat-number').text();
    const fId = flat.attr('questionData-flat-id');
    const fBuildNumber = flat.children('.gk-flat-build-number').text();
    const fFloor = flat.parent().parent().attr('questionData-floor-number');
    const fRizer = flat.parent().attr('questionData-rizer-number');
    const fPorch = flat.parent().parent().parent().attr('questionData-porch-number');
    var flatObject = {id : fId, porch : fPorch, floor : fFloor, flatNumber : fNumber, riser : fRizer, flatNumberBuild : fBuildNumber};
    return flatObject;
}
text_truncate = function(str, length, ending) {
    if (length == null) {
        length = 100;
    }
    if (ending == null) {
        ending = '...';
    }
    if (str.length > length) {
        return str.substring(0, length - ending.length) + ending;
    } else {
        return str;
    }
};


