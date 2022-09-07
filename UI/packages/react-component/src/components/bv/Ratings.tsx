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
import { Rating } from "@material-ui/lab";
type RatingSummaryProps = {
    value: number,
    count? :string,
};

/**
 * Rating  component
 * displays Rating stars wherever imported on the Product detail page. 
 * @param props
 */
export const Ratings: React.FC<RatingSummaryProps> = (props: any) => {
    const value = props.value;
    const count = props.count;

    return (
        <span style={{display:'flex'}}>
        <Rating
        name="rating-read"
        value={value}
        readOnly
        /> 
        {count === 'hide'?null:(
         <span style={{marginLeft:'8px'}}>({count?count:0})</span>
        )
        }
    </span>
    );
};
