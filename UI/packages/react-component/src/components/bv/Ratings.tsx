/**
*==================================================
Copyright [2022] [HCL America, Inc.]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*==================================================
**/
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
