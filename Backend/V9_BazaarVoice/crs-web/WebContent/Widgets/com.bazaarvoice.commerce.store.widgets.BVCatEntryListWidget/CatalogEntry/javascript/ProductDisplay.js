/**
	=================================================================
	Copyright [2021] [HCL America, Inc.]
	=================================================================
**/

productDisplayJS = {

    singleSKUProductWithoutDefiningAttribute: false,

    setCommonParameters: function (langId, storeId, catalogId, userType, currencySymbol) {
        shoppingActionsJS.setCommonParameters(langId, storeId, catalogId, userType, currencySymbol);
    },

    setEntitledItems: function (entitledItemArray) {
        shoppingActionsJS.setEntitledItems(entitledItemArray);
    },

    getCatalogEntryId: function (entitledItemId) {
        shoppingActionsJS.getCatalogEntryId(entitledItemId);
    },

    getEntitledItemJsonObject: function () {
        shoppingActionsJS.getEntitledItemJsonObject();
    },

    /**
     * registerAttributeIds Register the ids of HTML attributes that are associated with the specified attribute.
     *
     * @param {String} attributeName The name of the attribute.
     * @param {String} entitledItemId The element id where the json object of the sku is stored
     * @param {Object} ids Map of named HTML element ids
     *
     **/
    registerAttributeIds: function (attributeName, entitledItemId, ids) {
        shoppingActionsJS.registerAttributeIds(attributeName, entitledItemId, ids);
    },

    /**
     * setSelectedAttribute Sets the selected attribute value for a particular attribute not in reference to any catalog entry.
     *                     One place this function is used is on CachedProductOnlyDisplay.jsp where there is a drop down box of attributes.
     *                     When an attribute is selected from that drop down this method is called to update the selected value for that attribute.
     *
     * @param {String} selectedAttributeName The name of the attribute.
     * @param {String} selectedAttributeValue The value of the selected attribute.
     * @param {String} entitledItemId The element id where the json object of the sku is stored
     * @param {String} skuImageId This is optional. The element id of the product image - image element id is different in product page and category list view. Product page need not pass it because it is set separately
     * @param {String} imageField This is optional. The json field from which image should be picked. Pass value if a different size image need to be picked
     * @param {String} selectedAttributeDisplayValue This is optional. The attribute display value formatted with UOM
     *
     **/
    setSelectedAttribute: function (selectedAttributeName, selectedAttributeValue, entitledItemId, skuImageId, imageField, selectedAttributeDisplayValue) {
        shoppingActionsJS.setSelectedAttribute(selectedAttributeName, selectedAttributeValue, entitledItemId, skuImageId, imageField, selectedAttributeDisplayValue);
    },

    /**
     * resetSelectedAttribute Resets the the selected attribute value for the specified attribute.
     *
     * @param {String} attributeName The name of the attribute.
     * @param {String} entitledItemId The element id where the json object of the sku is stored
     * @param {String} productUrl The url of the parent product
     *
     **/
    resetSelectedAttribute: function (attributeName, entitledItemId) {
        var attributeName = attributeName.replace(shoppingActionsJS.search01, "\\\'").replace(shoppingActionsJS.search02, '\\\"');
        $("[id='attr_" + entitledItemId + '_' + attributeName + "']").removeClass("hidden");

        var attributeIds = shoppingActionsJS.getAttributeIds(attributeName, entitledItemId);
        if (attributeIds != null) {
            var selectWidget = $(attributeIds.selectAttributeValueId);
            if (selectWidget.length) {
                selectWidget.val("");
                selectWidget.Select("refresh");
            }
        }

        var dropdownList = shoppingActionsJS.allDropdownsList[entitledItemId];
        var remainingSelectedAttributes = {};

        for (var i = 0; i < dropdownList.length; i++) {
            if (dropdownList[i].name.replace(/\\'/g, "'") == attributeName.replace(/\\'/g, "'")) {
                $(dropdownList[i].node).removeClass("hidden");
                $(dropdownList[i].node).val('');
                $(dropdownList[i].node).Select("refresh");
                $(dropdownList[i].node).change();
            } else {
                for (var j = 0; j < dropdownList[i].options.length; j++) {
                    if (dropdownList[i].options[j].selected === true) {
                        remainingSelectedAttributes[dropdownList[i].name] = dropdownList[i].options[j].value;
                    }
                }
            }
        }

        var urlWithoutParams = document.location.href.split('?')[0];
        var params = "?";

        var productUrl = "";
        if ($("#ProductDisplayURL").length) {
            productUrl = $("#ProductDisplayURL").html();
        }

        var displaySKUContextData = false;
        if ($("#displaySKUContextData").length) {
            displaySKUContextData = eval('(' + $("#displaySKUContextData").html() + ')');
        }

        if (displaySKUContextData && urlWithoutParams !== productUrl) {
            for (attr in remainingSelectedAttributes) {
                if (remainingSelectedAttributes.hasOwnProperty(attr)) {
                    params += attr + '=' + remainingSelectedAttributes[attr] + '&';
                }
            }

            params = params.slice(0, -1);
            params = params.replace("\\\'", "'").replace('\\\"', '"');
            document.location.replace(productUrl + params);
        }
    },

    /**
     * setSelectedAttributeName Set the selected attribute name and make the drop-down associated with the attribute visible.
     *
     * @param {String} attributeName The name of the attribute.
     * @param {String} entitledItemId The element id where the json object of the sku is stored
     *
     **/
    setSelectedAttributeName: function (attributeName, entitledItemId) {
        var oldSelectedAttributeValuesId = shoppingActionsJS.selectedAttributeValues[entitledItemId];
        if (oldSelectedAttributeValuesId != null && oldSelectedAttributeValuesId !== "") {
            $("#" + oldSelectedAttributeValuesId).addClass("mobileHidden");
        }
        var selectedAttributeValuesId = null;
        var attributeIds = shoppingActionsJS.getAttributeIds(attributeName, entitledItemId);
        if (attributeIds != null) {
            selectedAttributeValuesId = attributeIds.attributeValuesId;
        }
        if (selectedAttributeValuesId != null && selectedAttributeValuesId !== "") {
            $("#" + selectedAttributeValuesId).removeClass("mobileHidden");
        }
        shoppingActionsJS.selectedAttributeValues[entitledItemId] = selectedAttributeValuesId;
    },

    /**
     * Add2ShopCartAjax This function is used to add a catalog entry to the shopping cart using an AJAX call. This will resolve the catentryId using entitledItemId and adds the item to the cart.
     *              This function will resolve the SKU based on the entitledItemId passed in and call {@link productDisplayJS.AddItem2ShopCartAjax}.
     * @param {String} entitledItemId A DIV containing a JSON object which holds information about a catalog entry. You can reference CachedProductOnlyDisplay.jsp to see how that div is constructed.
     * @param {int} quantity The quantity of the item to add to the cart.
     * @param {String} isPopup If the value is true, then this implies that the function was called from a quick info pop-up.
     * @param {Object} customParams - Any additional parameters that needs to be passed during service invocation.
     *
     **/
    Add2ShopCartAjax: function (entitledItemId, quantity, isPopup, customParams) {
        shoppingActionsJS.Add2ShopCartAjax(entitledItemId, quantity, isPopup, customParams);
    },

    /* SwatchCode start */

    /**
     * Sets the ID of the image to use for swatch.
     * @param {String} skuImageId The ID of the full image element.
     **/
    setSKUImageId: function (skuImageId) {
        shoppingActionsJS.setSKUImageId(skuImageId);
    },

    /**
     * changeViewImages Updates the angle views of the SKU.
     *
     * @param {String[]} itemAngleThumbnail An array of SKU view thumbnails.
     * @param {String[]} itemAngleFullImage An array of SKU view full images.
     * @param {String[]} itemAngleThumbnailShortDesc An array of short description for the SKU view thumbnails.
     **/
    changeViewImages: function (itemAngleThumbnail, itemAngleFullImage, itemAngleThumbnailShortDesc) {
        var imageCount = 0;
        for (x in itemAngleThumbnail) {
            var prodAngleCount = imageCount;
            imageCount++;

            var thumbnailWidgets = $("ul[id^='ProductAngleImagesAreaList']");
            if ($(thumbnailWidgets).length) {
                for (var i = 0; i < thumbnailWidgets.length; i++) {
                    if (null != thumbnailWidgets[i]) {
                        var angleThumbnail = document.createElement("li");
                        var angleThumbnailLink = document.createElement("a");
                        var angleThumbnailImg = document.createElement("img");

                        angleThumbnail.id = "productAngleLi" + prodAngleCount;

                        angleThumbnailLink.href = "JavaScript:changeThumbNail('productAngleLi" + prodAngleCount + "','" + itemAngleFullImage[x] + "');";
                        angleThumbnailLink.id = "WC_CachedProductOnlyDisplay_links_1_" + imageCount;
                        if (itemAngleThumbnailShortDesc != 'undefined' && itemAngleThumbnailShortDesc != null) {
                            angleThumbnailLink.title = itemAngleThumbnailShortDesc[x];
                        }

                        angleThumbnailImg.src = itemAngleThumbnail[x];
                        angleThumbnailImg.id = "WC_CachedProductOnlyDisplay_images_1_" + imageCount;
                        if (itemAngleThumbnailShortDesc != 'undefined' && itemAngleThumbnailShortDesc != null) {
                            angleThumbnailImg.alt = itemAngleThumbnailShortDesc[x];
                        }

                        if (prodAngleCount == 0) {
                            $(thumbnailWidgets[i]).empty();
                        }

                        angleThumbnailLink.appendChild(angleThumbnailImg);
                        angleThumbnail.appendChild(angleThumbnailLink);
                        thumbnailWidgets[i].appendChild(angleThumbnail);
                    }
                }
            }
        }

        var displayImageArea = "";
        if (imageCount > 0) {
            displayImageArea = 'block';
        } else {
            displayImageArea = 'none';
        }
        var angleImageArea = $("div[id^='ProductAngleImagesArea']");
        if (angleImageArea.length) {
            for (var i = 0; i < angleImageArea.length; i++) {
                if (null != angleImageArea[i]) {
                    $(angleImageArea[i]).css("display", displayImageArea);
                }
            }
        }
    },

    /**
     * Handles the case when a swatch is selected. Set the border of the selected swatch.
     * @param {String} selectedAttributeName The name of the selected swatch attribute.
     * @param {String} selectedAttributeValue The value of the selected swatch attribute.
     * @param {String} entitledItemId The ID of the SKU
     * @param {String} doNotDisable The name of the swatch attribute that should never be disabled.
     * @param {String} imageField, the field name from which the image should be picked
     * @return boolean Whether the swatch is available for selection
     **/
    selectSwatch: function (selectedAttributeName, selectedAttributeValue, entitledItemId, doNotDisable, selectedAttributeDisplayValue, skuImageId, imageField) {
        shoppingActionsJS.selectSwatch(selectedAttributeName, selectedAttributeValue, entitledItemId, doNotDisable, selectedAttributeDisplayValue, skuImageId, imageField);
    },

    /**
     * Constructs record and add to this.allSwatchesArray.
     * @param {String} swatchName The name of the swatch attribute.
     * @param {String} swatchValue The value of the swatch attribute.
     * @param {String} swatchImg1 The path to the swatch image.
    * @param {String} swatchImg2 The path to the swatch image representing disabled state.
     **/
    addToAllSwatchsArray: function(swatchName, swatchValue, swatchImg1, entitledItemId, swatchDisplayValue, swatchImg2) {
        shoppingActionsJS.addToAllSwatchsArray(swatchName, swatchValue, swatchImg1, entitledItemId, swatchDisplayValue, swatchImg2);
    },

    /**
     * Constructs record and add to this.allDropdownsArray.
     * @param {String} attributeName The name of the dropdown attribute.
     * @param {String} dropdownId The id of the dropdown.
     **/
    addToAllDropdownsArray: function (attributeName, dropdownId, entitledItemId) {
        shoppingActionsJS.addToAllDropdownsArray(attributeName, dropdownId, entitledItemId);
    },

    /* SwatchCode end */

    /** 
     * Displays price of the attribute selected with the catalog entry id.
     * 
     * @param {string} catEntryId The identifier of the sku.
     * @param {string} productId The identifier of the product.
     */
    displayPrice: function (catEntryId, productId) {
        var catEntry = shoppingActionsJS.itemPriceJsonOject[catEntryId].catalogEntry;

        var tempString;
        var popup = shoppingActionsJS.isPopup;

        if (popup == true) {
            $("#productPrice").html(catEntry.offerPrice);
        }

        if (popup == false) {
            var innerHTML = "";

            if (!catEntry.listPriced || catEntry.listPrice <= catEntry.offerPrice) {
                innerHTML = "<span id='offerPrice_" + catEntry.catalogEntryIdentifier.uniqueID + "' class='price'>" + catEntry.offerPrice + "</span>";
            } else {
                innerHTML = "<span id='listPrice_" + catEntry.catalogEntryIdentifier.uniqueID + "' class='old_price'>" + catEntry.listPrice + "</span>" +
                    "<span id='offerPrice_" + catEntry.catalogEntryIdentifier.uniqueID + "' class='price'>" + catEntry.offerPrice + "</span>";
            }

            if (flowEnabled.ProductTotalWithVAT) {
                innerHTML = innerHTML + "<span class='vat-include'><p>"+ Utils.getLocalizationMessage('VAT_INCL') +"</p></span>";  
            } 
            
            if (document.getElementById('price_display_' + productId)) {
                document.getElementById('price_display_' + productId).innerHTML = innerHTML + "<input type='hidden' id='ProductInfoPrice_" + catEntry.catalogEntryIdentifier.uniqueID + "' value='" + catEntry.offerPrice.replace(/"/g, "&#034;").replace(/'/g, "&#039;") + "'/>";
            } else if (document.getElementById('price_display_' + catEntryId)) {
                document.getElementById('price_display_' + catEntryId).innerHTML = innerHTML + "<input type='hidden' id='ProductInfoPrice_" + catEntry.catalogEntryIdentifier.uniqueID + "' value='" + catEntry.offerPrice.replace(/"/g, "&#034;").replace(/'/g, "&#039;") + "'/>";
            }
            
            innerHTML = "";
            if (shoppingActionsJS.displayPriceRange == true) {
                for (var i in catEntry.priceRange) {
                    if (catEntry.priceRange[i].endingNumberOfUnits == catEntry.priceRange[i].startingNumberOfUnits) {
                        tempString = Utils.getLocalizationMessage('PQ_PRICE_X', {
                            0: catEntry.priceRange[i].startingNumberOfUnits
                        });
                        innerHTML = innerHTML + "<p>" + tempString;
                    } else if (catEntry.priceRange[i].endingNumberOfUnits != 'null') {
                        tempString = Utils.getLocalizationMessage('PQ_PRICE_X_TO_Y', {
                            0: catEntry.priceRange[i].startingNumberOfUnits,
                            1: catEntry.priceRange[i].endingNumberOfUnits
                        });
                        innerHTML = innerHTML + "<p>" + tempString;
                    } else {
                        tempString = Utils.getLocalizationMessage('PQ_PRICE_X_OR_MORE', {
                            0: catEntry.priceRange[i].startingNumberOfUnits
                        });
                        innerHTML = innerHTML + "<p>" + tempString;
                    }
                    innerHTML = innerHTML + " <span class='price'>" + catEntry.priceRange[i].localizedPrice + "</span></p>";
                }
            }
            // Append productId so that element is unique in bundle page, where there can be multiple components
            if (document.getElementById('price_display_' + productId) && innerHTML !== '') {
                $("#price_display_" + productId).html($('#price_display_' + productId).html() + '<div class="item_spacer_3px"></div><div id="priceRange_' + productId + '" class="quantity_discount">' + Utils.getLocalizationMessage('PQ_PURCHASE') + innerHTML + '</div>');
            }
        }
    },

    /** 
     * Updates the product name in the NameAndPrice widget. 
     * 
     * @param {string} catEntryId The identifier of the sku.
     * @param {string} productId The identifier of the product.
     */
    updateProductName: function (catEntryId, productId) {
        var catEntry = shoppingActionsJS.itemPriceJsonOject[catEntryId].catalogEntry;

        if (shoppingActionsJS.isPopup == true) {
            $("#productName").html(catEntry.description[0].name);
        } else {
            if ($(".top > div[id^='PageHeading_']") != null) {
                $(".top > div[id^='PageHeading_']").each(function (i, node) {
                    if (node.childNodes != null && node.childNodes.length == 3) {
                        node.childNodes[1].innerHTML = catEntry.description[0].name;
                    }
                });
            }

            var productInfoWidgets = $("input[id^='ProductInfoName_" + productId + "']");
            if (productInfoWidgets != null) {
                for (var i = 0; i < productInfoWidgets.length; i++) {
                    if (productInfoWidgets[i] != null) {
                        $(productInfoWidgets[i]).val(catEntry.description[0].name);
                    }
                }
            }
        }
    },

    /** 
     * Updates the product part number in the NameAndPrice widget. 
     * 
     * @param {string} catEntryId The identifier of the sku.
     * @param {string} productId The identifier of the product.
     */
    updateProductPartNumber: function (catEntryId, productId) {
        var catEntry = shoppingActionsJS.itemPriceJsonOject[catEntryId].catalogEntry;

        if (shoppingActionsJS.isPopup == true) {
            $("#productSKUValue").html(catEntry.catalogEntryIdentifier.externalIdentifier.partNumber);
        } else {
            var partnumWidgets = $("span[id^='product_SKU_" + productId + "']");
            if (partnumWidgets != null) {
                for (var i = 0; i < partnumWidgets.length; i++) {
                    if ($(partnumWidgets[i])) {
                        $(partnumWidgets[i]).html(Utils.getLocalizationMessage('SKU') + " " + catEntry.catalogEntryIdentifier.externalIdentifier.partNumber);
                    }
                }
            }
        }
    },

    /** 
     * Updates the product short description in the ShortDescription widget. 
     * 
     * @param {string} catEntryId The identifier of the sku.
     * @param {string} productId The identifier of the product.
     */
    updateProductShortDescription: function (catEntryId, productId) {
        var catEntry = shoppingActionsJS.itemPriceJsonOject[catEntryId].catalogEntry;

        var shortDescWidgets = $("p[id^='product_shortdescription_" + productId + "']");
        if (shortDescWidgets != null) {
            for (var i = 0; i < shortDescWidgets.length; i++) {
                if (shortDescWidgets[i]) {
                    shortDescWidgets[i].innerHTML = catEntry.description[0].shortDescription;
                }
            }
        }
    },

    /** 
     * Updates the product long description in the LongDescription widget. 
     * 
     * @param {string} catEntryId The identifier of the sku.
     * @param {string} productId The identifier of the product.
     */
    updateProductLongDescription: function (catEntryId, productId) {
        var catEntry = shoppingActionsJS.itemPriceJsonOject[catEntryId].catalogEntry;

        var longDescWidgets = $("p[id^='product_longdescription_" + productId + "']");
        if (longDescWidgets != null) {
            for (var i = 0; i < longDescWidgets.length; i++) {
                if (longDescWidgets[i]) {
                    longDescWidgets[i].innerHTML = catEntry.description[0].longDescription;
                }
            }
        }
    },

    /** 
     * Updates the product discounts in the Discounts widget. 
     * 
     * @param {string} catEntryId The identifier of the sku.
     * @param {string} productId The identifier of the product.
     */
    updateProductDiscount: function (catEntryId, productId) {
        var catEntry = shoppingActionsJS.itemPriceJsonOject[catEntryId].catalogEntry;

        var newHtml = '';
        if (typeof catEntry.discounts != 'undefined') {
            for (var i = 0; i < catEntry.discounts.length; i++) {
                if (i > 0) {
                    newHtml += '<div class="clear_float"></div><div class="item_spacer_2px"></div>';
                }
                /* catEntry.discounts[i].description comes from short description associated with the promotion.
                 * If it is blank or missing, the link text is blank, thus is not clickable or displayed.
                 */
                newHtml += '<a class="promotion" href="' + catEntry.discounts[i].url + '">' + catEntry.discounts[i].description + '</a>';
            }
        }

        var discountWidgets = $("div[id^='Discounts_']");
        if (discountWidgets != null) {
            for (var i = 0; i < discountWidgets.length; i++) {
                if (discountWidgets[i]) {
                    discountWidgets[i].innerHTML = newHtml;
                }
            }
        }
    },

    /** 
     * Updates the product full image and angle images in the FullImage widget. 
     * 
     * @param {string} catEntryId The identifier of the sku.
     * @param {string} productId The identifier of the product.
     */
    updateProductImage: function (catEntryId, productId) {
        var newFullImage = null;
        var newAngleThumbnail = null;
        var newAngleFullImage = null;
        var newAngleThumbnailShortDesc = null;

        var skuFullImageFromService = null;
        var catEntry = null;
        if (shoppingActionsJS.itemPriceJsonOject[catEntryId] != null) {
            catEntry = shoppingActionsJS.itemPriceJsonOject[catEntryId].catalogEntry;
        }
        if (catEntry != null) {
            skuFullImageFromService = catEntry.description[0].fullImage;
        }

        var entitledItemId = "entitledItem_" + productId;
        var imageArr = shoppingActionsJS.getImageForSKU(entitledItemId);
        if (imageArr != null) {
            newFullImage = imageArr[0];
            newAngleThumbnail = imageArr[2];
            newAngleFullImage = imageArr[3];
            newAngleThumbnailShortDesc = imageArr[4];
        }

        if (catEntryId != null) {
            if (skuFullImageFromService != null && skuFullImageFromService !== "") {
                newFullImage = skuFullImageFromService;
            }
        } else if (productId != null && this.singleSKUProductWithoutDefiningAttribute) {
            newFullImage = shoppingActionsJS.entitledItems[0].ItemImage467;
            if (skuFullImageFromService != null && skuFullImageFromService !== "") {
                newFullImage = skuFullImageFromService;
            }
            newAngleThumbnail = shoppingActionsJS.entitledItems[0].ItemAngleThumbnail;
            newAngleFullImage = shoppingActionsJS.entitledItems[0].ItemAngleFullImage;
            newAngleThumbnailShortDesc = shoppingActionsJS.entitledItems[0].ItemAngleThumbnailShortDesc;
        } else {
            var imageFound = false;
            var selectedAttributes = shoppingActionsJS.selectedAttributesList[entitledItemId];
            for (x in shoppingActionsJS.entitledItems) {
                var Attributes = shoppingActionsJS.entitledItems[x].Attributes;

                for (attribute in selectedAttributes) {
                    var matchingAttributeFound = false;
                    if (selectedAttributes[attribute] !== '') {
                        for (y in Attributes) {
                            var index = y.indexOf("_|_");
                            var entitledSwatchName = y.substring(0, index);
                            var entitledSwatchValue = y.substring(index + 3);

                            if (entitledSwatchName == attribute && entitledSwatchValue == selectedAttributes[attribute]) {
                                matchingAttributeFound = true;
                                break;
                            }
                        }

                        // No matching attributes found, so this is not the correct SKU
                        if (!matchingAttributeFound) {
                            imageFound = false;
                            break;
                        }
                        imageFound = true;
                    }
                }

                // imageFound will only be true if all attributes have been matched (so the first correct SKU)
                if (imageFound) {
                    newFullImage = shoppingActionsJS.entitledItems[x].ItemImage467;
                    newAngleThumbnail = shoppingActionsJS.entitledItems[x].ItemAngleThumbnail;
                    newAngleFullImage = shoppingActionsJS.entitledItems[x].ItemAngleFullImage;
                    newAngleThumbnailShortDesc = shoppingActionsJS.entitledItems[x].ItemAngleThumbnailShortDesc;
                    break;
                }
            }
        }

        var imgWidgets = $("img[id^='" + shoppingActionsJS.skuImageId + "']");
        for (var i = 0; i < imgWidgets.length; i++) {
            if (imgWidgets[i] != null && newFullImage != null) {
                imgWidgets[i].src = newFullImage;
            }
        }

        var productImgWidgets = $("input[id^='ProductInfoImage_" + productId + "']");
        for (var i = 0; i < productImgWidgets.length; i++) {
            if (productImgWidgets[i] != null && newFullImage != null) {
                productImgWidgets[i].value = newFullImage;
            }
        }

        var prodAngleImageArea = $("div[id^='ProductAngleProdImagesArea']");
        var skuAngleImageArea = $("div[id^='ProductAngleImagesArea']");

        if (newAngleThumbnail != null && newAngleFullImage != null) {
            if (prodAngleImageArea.length) {
                for (var i = 0; i < prodAngleImageArea.length; i++) {
                    if (null != prodAngleImageArea[i]) {
                        $(prodAngleImageArea[i]).css("display", 'none');
                    }
                }
            }
            shoppingActionsJS.changeViewImages(newAngleThumbnail, newAngleFullImage, newAngleThumbnailShortDesc);
        } else {
            var prodDisplayClass = 'block';
            var selectedAttributes = shoppingActionsJS.selectedAttributesList[entitledItemId];
            for (attribute in selectedAttributes) {
                if (null != selectedAttributes[attribute] && '' != selectedAttributes[attribute]) {
                    prodDisplayClass = 'none';
                }
            }

            if (prodAngleImageArea.length) {
                for (var i = 0; i < prodAngleImageArea.length; i++) {
                    if (prodAngleImageArea[i].length) {
                        $(prodAngleImageArea[i]).css("display", prodDisplayClass);
                    }
                }
            }

            if (skuAngleImageArea != null) {
                for (var i = 0; i < skuAngleImageArea.length; i++) {
                    if (skuAngleImageArea[i].length) {
                        $(skuAngleImageArea[i]).css("display", 'none');
                    }
                }
            }
        }
    },

    /** 
     * To notify the change in attribute to other components that is subscribed to 'DefiningAttributes_Changed' or 'DefiningAttributes_Resolved' event.
     * 
     * @param {string} productId The identifier of the product.
     * @param {string} entitledItemId The identifier of the entitled item.
     * @param {boolean} isPopup If the value is true, then this implies that the function was called from a quick info pop-up.
     * @param {boolean} displayPriceRange A boolean used to to determine whether or not to display the price range when the catEntry is selected.
     */
    notifyAttributeChange: function (productId, entitledItemId, isPopup, displayPriceRange) {
        shoppingActionsJS.baseCatalogEntryId = productId;
        var selectedAttributes = shoppingActionsJS.selectedAttributesList[entitledItemId];

        shoppingActionsJS.displayPriceRange = displayPriceRange;
        shoppingActionsJS.isPopup = isPopup;

        var catalogEntryId = null;
        if (shoppingActionsJS.selectedProducts[productId]) {
            catalogEntryId = shoppingActionsJS.getCatalogEntryIdforProduct(shoppingActionsJS.selectedProducts[productId]);
        } else {
            catalogEntryId = shoppingActionsJS.getCatalogEntryId(entitledItemId);
        }

        if (catalogEntryId != null) {
            wcTopic.publish('DefiningAttributes_Resolved_' + productId, catalogEntryId, productId);

            //check if the json object is already present for the catEntry.
            var catEntry = shoppingActionsJS.itemPriceJsonOject[catalogEntryId];

            if (catEntry != null && catEntry != undefined) {
                productDisplayJS.publishAttributeResolvedEvent(catalogEntryId, productId);
            }
            //if json object is not present, call the service to get the details.
            else {
                var parameters = {
                    storeId: shoppingActionsJS.storeId,
                    langId: shoppingActionsJS.langId,
                    catalogId: shoppingActionsJS.catalogId,
                    catalogEntryId: catalogEntryId,
                    productId: productId
                };

                //Declare a service for retrieving catalog entry detailed information for an item...
                wcService.declare({
                    id: "getCatalogEntryDetails",
                    actionId: "getCatalogEntryDetails",
                    url: getAbsoluteURL() + appendWcCommonRequestParameters("GetCatalogEntryDetailsByIDView"),
                    formId: ""

                    ,
                    successHandler: function (serviceResponse, ioArgs) {
                        productDisplayJS.publishAttributeResolvedEventServiceResponse(serviceResponse, ioArgs);
                    }

                    ,
                    failureHandler: function (serviceResponse, ioArgs) {
                        console.debug("productDisplayJS.notifyAttributeChange: Unexpected error occurred during an xhrPost request.");
                    }

                });
                wcService.invoke("getCatalogEntryDetails", parameters);
            }
        } else {
            wcTopic.publish('DefiningAttributes_Changed', catalogEntryId, productId);
            wcTopic.publish('DefiningAttributes_Changed_' + productId, catalogEntryId, productId);
            console.debug("Publishing event 'DefiningAttributes_Changed' with params: catEntryId=" + catalogEntryId + ", productId=" + productId);
        }
    },

    /** 
     * Publishes the 'DefiningAttributes_Resolved' event using the JSON object returned from the server.
     * 
     * @param {object} serviceRepsonse The JSON response from the service.
     * @param {object} ioArgs The arguments from the service call.
     */
    publishAttributeResolvedEventServiceResponse: function (serviceResponse, ioArgs) {
        var productId = ioArgs.productId;
        //stores the json object, so that the service is not called when same catEntry is selected.
        shoppingActionsJS.itemPriceJsonOject[serviceResponse.catalogEntry.catalogEntryIdentifier.uniqueID] = serviceResponse;

        this.publishAttributeResolvedEvent(serviceResponse.catalogEntry.catalogEntryIdentifier.uniqueID, productId);
    },

    /** 
     * Publishes the 'DefiningAttributes_Resolved' event with the necessary parameters. 
     * 
     * @param {string} catEntryId The identifier of the sku.
     * @param {string} productId The identifier of the product.
     */
    publishAttributeResolvedEvent: function (catEntryId, productId) {
        if (!shoppingActionsJS.isPopup) {
            if (this.entitledItems) {
                for (x in this.entitledItems) {
                    var sku = this.entitledItems[x];
                    if (sku.catentry_id === catEntryId) {
                        if (sku.displaySKUContextData === 'true') {
                            if (document.location.href !== sku.seo_url) {
                                document.location.replace(sku.seo_url);
                            } else {
                                wcTopic.publish('DefiningAttributes_Resolved', catEntryId, productId);
                                console.debug("Publishing event 'DefiningAttributes_Resolved' with params: catEntryId=" + catEntryId + ", productId=" + productId);
                            }
                        } else {
                            wcTopic.publish('DefiningAttributes_Resolved', catEntryId, productId);
                            console.debug("Publishing event 'DefiningAttributes_Resolved' with params: catEntryId=" + catEntryId + ", productId=" + productId);
                        }
                    }
                }
            } else {
                console.debug("Publishing event 'DefiningAttributes_Resolved' with params: catEntryId=" + catEntryId + ", productId=" + productId);
                wcTopic.publish('DefiningAttributes_Resolved', catEntryId, productId);
            }
        }
    },

    /**
     * To notify the change in quantity to other components that is subscribed to ShopperActions_Changed event.
     */
    notifyQuantityChange: function (quantity) {
        wcTopic.publish('ShopperActions_Changed', quantity);
        console.debug("Publishing event 'ShopperActions_Changed' with params: quantity=" + quantity);
    },

    /**
     * To display attachment page.
     */
    showAttachmentPage: function (data) {
        var pageNumber = data['pageNumber'];
        var pageSize = data['pageSize'];
        pageNumber = Number(pageNumber);
        pageSize = Number(pageSize);

        setCurrentId(data["linkId"]);

        if (!submitRequest()) {
            return;
        }

        var beginIndex = pageSize * (pageNumber - 1);
        cursor_wait();
        wcRenderContext.updateRenderContext('AttachmentPagination_Context', {
            'beginIndex': beginIndex
        });
        MessageHelper.hideAndClearMessage();
    },

    /**
     * Register mouse down event 
     */
    registerMouseDown: function (node) {
        $(node).on("mousedown",
            function () {
                productDisplayJS.calculateScrollingHeight(node);
            }
        );
    },

    /**
     * Calculate scrolling height.
     */
    calculateScrollingHeight: function (node) {
        var selectedNode = shoppingActionsJS.findDropdownById(node);
        var nodePosition = null;
        if (selectedNode) {
            nodePosition = Utils.position(selectedNode);
        } else {
            return;
        }
        var windowHeight = window.innerHeight;
        if (windowHeight - nodePosition.y > nodePosition.y) {
            var newHeight = windowHeight - nodePosition.y;
        } else {
            var newHeight = nodePosition.y;
        }
        if ($("#" + node + "_dropdown").length) {
            if (windowHeight - nodePosition.y > nodePosition.y) {
                var newHeight = windowHeight - nodePosition.y;
            } else {
                var newHeight = nodePosition.y;
            }
            var dropdownHeight = $("#" + node + "_dropdown").clientHeight;
            if (dropdownHeight > newHeight) {
                $("#" + node + "_dropdown").css("height", newHeight + "px");
            }
        }
    }
}
$(document).ready(function () {
    var ie_version = Utils.get_IE_version();
    if (ie_version < 9) {
        $(document).on("click", ".compare_target > input[type=\"checkbox\"]", function (event) {
            this.blur();
            this.focus();
        });
    }
});
