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
type RatingSummaryProps = {
    content: string,
    productId :string,
};

/**
 * Rating Summary component
 * displays Rating Summary on the Product detail page. 
 * @param props
 */
export const RatingSummary: React.FC<RatingSummaryProps> = (props: any) => {
    const content = props.content?props.content:'';
    const productId= props.productId;

    return (
        <div data-bv-show="rating_summary" data-bv-productid={'11501_11_'+productId}>
            {parse(content)}
    </div>
    );
};
