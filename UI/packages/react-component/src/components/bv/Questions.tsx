/*
 *==================================================
 * Licensed Materials - Property of HCL Technologies
 *
 * HCL Commerce
 *
 * (C) Copyright HCL Technologies Limited 2020
 *
 *==================================================
 */
//Standard libraries
import React from "react";
import parse from 'html-react-parser';
type QuestionsProps = {
    content: string,
    productId :string,
};

/**
 * Questions component
 * displays Questions on the Product detail page. 
 * @param props
 */
export const Questions: React.FC<QuestionsProps> = (props: any) => {
    const content = props.content?props.content:'';
    const productId= props.productId;

    return (
        <div data-bv-show="questions" data-bv-productid={'11501_11_'+productId}>
            {parse(content)}
    </div>
    );
};
