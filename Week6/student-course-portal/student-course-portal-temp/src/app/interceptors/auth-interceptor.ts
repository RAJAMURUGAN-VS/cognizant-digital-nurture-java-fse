import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authReq = req.clone({

    setHeaders: {

      Authorization: 'Bearer mock-token-12345'

    }

  });

  return next(authReq);

};