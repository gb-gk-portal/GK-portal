function constructFlatObject(flat) {
    const fNumber = flat.children('.gk-flat-number').text();
    const fId = flat.attr('data-flat-id');
    const fBuildNumber = flat.children('.gk-flat-build-number').text();
    const fFloor = flat.parent().parent().attr('data-floor-number');
    const fRizer = flat.parent().attr('data-rizer-number');
    const fPorch = flat.parent().parent().parent().attr('data-porch-number');
    var flatObject = {id : fId, porch : fPorch, floor : fFloor, flatNumber : fNumber, riser : fRizer, flatNumberBuild : fBuildNumber};
    return flatObject;
}


