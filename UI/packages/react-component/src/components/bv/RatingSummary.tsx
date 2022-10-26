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
