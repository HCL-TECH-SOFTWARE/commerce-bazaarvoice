/*
 *==================================================
 * Licensed Materials - Property of HCL America, Inc.
 *
 * HCL Commerce
 *
 * (C) Copyright HCL America, Inc. Limited 2020
 *
 *==================================================
 */
//Standard libraries
import React from "react";

import parse from 'html-react-parser';
type ReviewsProps = {
    content: string,
    productId :string,
};

/**
 * Reviews component
 * displays Reviews on the Product detail page. 
 * @param props
 */
export const Reviews: React.FC<ReviewsProps> = (props: any) => {
    const content = props.content?props.content:'';
    const productId= props.productId;

    return (
        <div data-bv-show="reviews" data-bv-productid={'11501_11_'+productId}>
            {parse(content)}
    </div>
    );
};
