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
import Axios, { AxiosPromise, AxiosRequestConfig, Canceler } from "axios";
import { getSite } from "../../hooks/useSite";
import { storageSessionHandler } from "../../utils/storageUtil";

const CancelToken = Axios.CancelToken;
const cancels: Canceler[] = [];
const payloadBase: any = {
    cancelToken: new CancelToken(function executor(c) {
      cancels.push(c);
    }),
  };
  const payload = {
    ...payloadBase,
  };

const bVService = {

getRatingAndReviews(productId): AxiosPromise<any> {
    const storeID = getSite()?.storeID;
    const catID = getSite()?.catalogID;
    productId= catID+'_'+storeID+'_'+productId;
    const baseURI=window.location.pathname;
    const currentUser = storageSessionHandler.getCurrentUserAndLoadAccount();
    const requestOptions: AxiosRequestConfig = Object.assign({
        url: "/wcs/resources/store/"+storeID+"/bazaarvoice/content?subjectId="+productId+"&baseURI="+baseURI+"&pageURI="+baseURI,
        method: "GET",
    });
    return Axios(requestOptions);
}
}

export default bVService; 
