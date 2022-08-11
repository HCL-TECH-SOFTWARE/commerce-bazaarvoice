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
