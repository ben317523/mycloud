var N=Object.prototype.hasOwnProperty,O=Array.isArray,m=function(){for(var l=[],e=0;e<256;++e)l.push("%"+((e<16?"0":"")+e.toString(16)).toUpperCase());return l}(),H=function(e){for(;e.length>1;){var r=e.pop(),n=r.obj[r.prop];if(O(n)){for(var i=[],t=0;t<n.length;++t)typeof n[t]!="undefined"&&i.push(n[t]);r.obj[r.prop]=i}}},C=function(e,r){for(var n=r&&r.plainObjects?Object.create(null):{},i=0;i<e.length;++i)typeof e[i]!="undefined"&&(n[i]=e[i]);return n},z=function l(e,r,n){if(!r)return e;if(typeof r!="object"){if(O(e))e.push(r);else if(e&&typeof e=="object")(n&&(n.plainObjects||n.allowPrototypes)||!N.call(Object.prototype,r))&&(e[r]=!0);else return[e,r];return e}if(!e||typeof e!="object")return[e].concat(r);var i=e;return O(e)&&!O(r)&&(i=C(e,n)),O(e)&&O(r)?(r.forEach(function(t,f){if(N.call(e,f)){var a=e[f];a&&typeof a=="object"&&t&&typeof t=="object"?e[f]=l(a,t,n):e.push(t)}else e[f]=t}),e):Object.keys(r).reduce(function(t,f){var a=r[f];return N.call(t,f)?t[f]=l(t[f],a,n):t[f]=a,t},i)},B=function(e,r){return Object.keys(r).reduce(function(n,i){return n[i]=r[i],n},e)},R=function(l,e,r){var n=l.replace(/\+/g," ");if(r==="iso-8859-1")return n.replace(/%[0-9a-f]{2}/gi,unescape);try{return decodeURIComponent(n)}catch{return n}},I=function(e,r,n){if(e.length===0)return e;var i=typeof e=="string"?e:String(e);if(n==="iso-8859-1")return escape(i).replace(/%u[0-9a-f]{4}/gi,function(c){return"%26%23"+parseInt(c.slice(2),16)+"%3B"});for(var t="",f=0;f<i.length;++f){var a=i.charCodeAt(f);if(a===45||a===46||a===95||a===126||a>=48&&a<=57||a>=65&&a<=90||a>=97&&a<=122){t+=i.charAt(f);continue}if(a<128){t=t+m[a];continue}if(a<2048){t=t+(m[192|a>>6]+m[128|a&63]);continue}if(a<55296||a>=57344){t=t+(m[224|a>>12]+m[128|a>>6&63]+m[128|a&63]);continue}f+=1,a=65536+((a&1023)<<10|i.charCodeAt(f)&1023),t+=m[240|a>>18]+m[128|a>>12&63]+m[128|a>>6&63]+m[128|a&63]}return t},V=function(e){for(var r=[{obj:{o:e},prop:"o"}],n=[],i=0;i<r.length;++i)for(var t=r[i],f=t.obj[t.prop],a=Object.keys(f),c=0;c<a.length;++c){var o=a[c],s=f[o];typeof s=="object"&&s!==null&&n.indexOf(s)===-1&&(r.push({obj:f,prop:o}),n.push(s))}return H(r),e},U=function(e){return Object.prototype.toString.call(e)==="[object RegExp]"},K=function(e){return!e||typeof e!="object"?!1:!!(e.constructor&&e.constructor.isBuffer&&e.constructor.isBuffer(e))},G=function(e,r){return[].concat(e,r)},Q={arrayToObject:C,assign:B,combine:G,compact:V,decode:R,encode:I,isBuffer:K,isRegExp:U,merge:z},W=String.prototype.replace,_=/%20/g,L={default:"RFC3986",formatters:{RFC1738:function(l){return W.call(l,_,"+")},RFC3986:function(l){return l}},RFC1738:"RFC1738",RFC3986:"RFC3986"},T=Q,x=L,J=Object.prototype.hasOwnProperty,A={brackets:function(e){return e+"[]"},comma:"comma",indices:function(e,r){return e+"["+r+"]"},repeat:function(e){return e}},g=Array.isArray,M=Array.prototype.push,F=function(l,e){M.apply(l,g(e)?e:[e])},X=Date.prototype.toISOString,y={addQueryPrefix:!1,allowDots:!1,charset:"utf-8",charsetSentinel:!1,delimiter:"&",encode:!0,encoder:T.encode,encodeValuesOnly:!1,formatter:x.formatters[x.default],indices:!1,serializeDate:function(e){return X.call(e)},skipNulls:!1,strictNullHandling:!1},Y=function l(e,r,n,i,t,f,a,c,o,s,v,h,p){var u=e;if(typeof a=="function"?u=a(r,u):u instanceof Date?u=s(u):n==="comma"&&g(u)&&(u=u.join(",")),u===null){if(i)return f&&!h?f(r,y.encoder,p):r;u=""}if(typeof u=="string"||typeof u=="number"||typeof u=="boolean"||T.isBuffer(u)){if(f){var $=h?r:f(r,y.encoder,p);return[v($)+"="+v(f(u,y.encoder,p))]}return[v(r)+"="+v(String(u))]}var w=[];if(typeof u=="undefined")return w;var S;if(g(a))S=a;else{var D=Object.keys(u);S=c?D.sort(c):D}for(var E=0;E<S.length;++E){var b=S[E];t&&u[b]===null||(g(u)?F(w,l(u[b],typeof n=="function"?n(r,b):r,n,i,t,f,a,c,o,s,v,h,p)):F(w,l(u[b],r+(o?"."+b:"["+b+"]"),n,i,t,f,a,c,o,s,v,h,p)))}return w},Z=function(e){if(!e)return y;if(e.encoder!==null&&e.encoder!==void 0&&typeof e.encoder!="function")throw new TypeError("Encoder has to be a function.");var r=e.charset||y.charset;if(typeof e.charset!="undefined"&&e.charset!=="utf-8"&&e.charset!=="iso-8859-1")throw new TypeError("The charset option must be either utf-8, iso-8859-1, or undefined");var n=x.default;if(typeof e.format!="undefined"){if(!J.call(x.formatters,e.format))throw new TypeError("Unknown format option provided.");n=e.format}var i=x.formatters[n],t=y.filter;return(typeof e.filter=="function"||g(e.filter))&&(t=e.filter),{addQueryPrefix:typeof e.addQueryPrefix=="boolean"?e.addQueryPrefix:y.addQueryPrefix,allowDots:typeof e.allowDots=="undefined"?y.allowDots:!!e.allowDots,charset:r,charsetSentinel:typeof e.charsetSentinel=="boolean"?e.charsetSentinel:y.charsetSentinel,delimiter:typeof e.delimiter=="undefined"?y.delimiter:e.delimiter,encode:typeof e.encode=="boolean"?e.encode:y.encode,encoder:typeof e.encoder=="function"?e.encoder:y.encoder,encodeValuesOnly:typeof e.encodeValuesOnly=="boolean"?e.encodeValuesOnly:y.encodeValuesOnly,filter:t,formatter:i,serializeDate:typeof e.serializeDate=="function"?e.serializeDate:y.serializeDate,skipNulls:typeof e.skipNulls=="boolean"?e.skipNulls:y.skipNulls,sort:typeof e.sort=="function"?e.sort:null,strictNullHandling:typeof e.strictNullHandling=="boolean"?e.strictNullHandling:y.strictNullHandling}},q=function(l,e){var r=l,n=Z(e),i,t;typeof n.filter=="function"?(t=n.filter,r=t("",r)):g(n.filter)&&(t=n.filter,i=t);var f=[];if(typeof r!="object"||r===null)return"";var a;e&&e.arrayFormat in A?a=e.arrayFormat:e&&"indices"in e?a=e.indices?"indices":"repeat":a="indices";var c=A[a];i||(i=Object.keys(r)),n.sort&&i.sort(n.sort);for(var o=0;o<i.length;++o){var s=i[o];n.skipNulls&&r[s]===null||F(f,Y(r[s],s,c,n.strictNullHandling,n.skipNulls,n.encode?n.encoder:null,n.filter,n.sort,n.allowDots,n.serializeDate,n.formatter,n.encodeValuesOnly,n.charset))}var v=f.join(n.delimiter),h=n.addQueryPrefix===!0?"?":"";return n.charsetSentinel&&(n.charset==="iso-8859-1"?h+="utf8=%26%2310003%3B&":h+="utf8=%E2%9C%93&"),v.length>0?h+v:""},j=Q,P=Object.prototype.hasOwnProperty,d={allowDots:!1,allowPrototypes:!1,arrayLimit:20,charset:"utf-8",charsetSentinel:!1,comma:!1,decoder:j.decode,delimiter:"&",depth:5,ignoreQueryPrefix:!1,interpretNumericEntities:!1,parameterLimit:1e3,parseArrays:!0,plainObjects:!1,strictNullHandling:!1},k=function(l){return l.replace(/&#(\d+);/g,function(e,r){return String.fromCharCode(parseInt(r,10))})},ee="utf8=%26%2310003%3B",re="utf8=%E2%9C%93",ne=function(e,r){var n={},i=r.ignoreQueryPrefix?e.replace(/^\?/,""):e,t=r.parameterLimit===1/0?void 0:r.parameterLimit,f=i.split(r.delimiter,t),a=-1,c,o=r.charset;if(r.charsetSentinel)for(c=0;c<f.length;++c)f[c].indexOf("utf8=")===0&&(f[c]===re?o="utf-8":f[c]===ee&&(o="iso-8859-1"),a=c,c=f.length);for(c=0;c<f.length;++c)if(c!==a){var s=f[c],v=s.indexOf("]="),h=v===-1?s.indexOf("="):v+1,p,u;h===-1?(p=r.decoder(s,d.decoder,o),u=r.strictNullHandling?null:""):(p=r.decoder(s.slice(0,h),d.decoder,o),u=r.decoder(s.slice(h+1),d.decoder,o)),u&&r.interpretNumericEntities&&o==="iso-8859-1"&&(u=k(u)),u&&r.comma&&u.indexOf(",")>-1&&(u=u.split(",")),P.call(n,p)?n[p]=j.combine(n[p],u):n[p]=u}return n},ae=function(l,e,r){for(var n=e,i=l.length-1;i>=0;--i){var t,f=l[i];if(f==="[]"&&r.parseArrays)t=[].concat(n);else{t=r.plainObjects?Object.create(null):{};var a=f.charAt(0)==="["&&f.charAt(f.length-1)==="]"?f.slice(1,-1):f,c=parseInt(a,10);!r.parseArrays&&a===""?t={0:n}:!isNaN(c)&&f!==a&&String(c)===a&&c>=0&&r.parseArrays&&c<=r.arrayLimit?(t=[],t[c]=n):t[a]=n}n=t}return n},te=function(e,r,n){if(!!e){var i=n.allowDots?e.replace(/\.([^.[]+)/g,"[$1]"):e,t=/(\[[^[\]]*])/,f=/(\[[^[\]]*])/g,a=t.exec(i),c=a?i.slice(0,a.index):i,o=[];if(c){if(!n.plainObjects&&P.call(Object.prototype,c)&&!n.allowPrototypes)return;o.push(c)}for(var s=0;(a=f.exec(i))!==null&&s<n.depth;){if(s+=1,!n.plainObjects&&P.call(Object.prototype,a[1].slice(1,-1))&&!n.allowPrototypes)return;o.push(a[1])}return a&&o.push("["+i.slice(a.index)+"]"),ae(o,r,n)}},ie=function(e){if(!e)return d;if(e.decoder!==null&&e.decoder!==void 0&&typeof e.decoder!="function")throw new TypeError("Decoder has to be a function.");if(typeof e.charset!="undefined"&&e.charset!=="utf-8"&&e.charset!=="iso-8859-1")throw new Error("The charset option must be either utf-8, iso-8859-1, or undefined");var r=typeof e.charset=="undefined"?d.charset:e.charset;return{allowDots:typeof e.allowDots=="undefined"?d.allowDots:!!e.allowDots,allowPrototypes:typeof e.allowPrototypes=="boolean"?e.allowPrototypes:d.allowPrototypes,arrayLimit:typeof e.arrayLimit=="number"?e.arrayLimit:d.arrayLimit,charset:r,charsetSentinel:typeof e.charsetSentinel=="boolean"?e.charsetSentinel:d.charsetSentinel,comma:typeof e.comma=="boolean"?e.comma:d.comma,decoder:typeof e.decoder=="function"?e.decoder:d.decoder,delimiter:typeof e.delimiter=="string"||j.isRegExp(e.delimiter)?e.delimiter:d.delimiter,depth:typeof e.depth=="number"?e.depth:d.depth,ignoreQueryPrefix:e.ignoreQueryPrefix===!0,interpretNumericEntities:typeof e.interpretNumericEntities=="boolean"?e.interpretNumericEntities:d.interpretNumericEntities,parameterLimit:typeof e.parameterLimit=="number"?e.parameterLimit:d.parameterLimit,parseArrays:e.parseArrays!==!1,plainObjects:typeof e.plainObjects=="boolean"?e.plainObjects:d.plainObjects,strictNullHandling:typeof e.strictNullHandling=="boolean"?e.strictNullHandling:d.strictNullHandling}},fe=function(l,e){var r=ie(e);if(l===""||l===null||typeof l=="undefined")return r.plainObjects?Object.create(null):{};for(var n=typeof l=="string"?ne(l,r):l,i=r.plainObjects?Object.create(null):{},t=Object.keys(n),f=0;f<t.length;++f){var a=t[f],c=te(a,n[a],r);i=j.merge(i,c,r)}return j.compact(i)},le=q,ce=fe,ue=L,oe={formats:ue,parse:ce,stringify:le};export{oe as l};