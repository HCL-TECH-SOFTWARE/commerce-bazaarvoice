## Implementation guide for Bazarvoice Storefront changes
  /**
     * import the baazaar voice script
     * 
     */
      const script = document.createElement("script");  
      script.async = true;   
      script.src = "https://apps.bazaarvoice.com/deployments/partner-hcl/main_site/staging/en_US/bv.js"; 

      document.body.appendChild(script);
      
      Once the libarary is loaded, please update the above mentioned UI files in the 9.1.10.0 React store code base.
      
     ## Refer the Implementation guide word document.
