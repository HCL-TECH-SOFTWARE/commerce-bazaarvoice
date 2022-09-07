/**
	*==================================================
	Copyright [2021] [HCL America, Inc.]
	*==================================================
**/

dojo.subscribe("CMPageRefreshEvent",function(){
    var bvContainerPrefix = 'BVRRInlineRating';
    var bvContainerPrefixLength = bvContainerPrefix.length + 1;
    var productContainer = dojo.query(".product_listing_container .product_review_js");
    if(productContainer != null && productContainer.length > 0){
        var partNumberArry = new Array();
        var bvReviewContainerId;
        
        for(i=0; i < productContainer.length; i++){
            bvReviewContainerId = dojo.attr(productContainer[i], "id").trim();
            if(bvReviewContainerId.length > bvContainerPrefixLength)
                partNumberArry[i] = bvReviewContainerId.substring(bvContainerPrefixLength);
            else partNumberArry[i] = '';
        }
        
     
    }
});