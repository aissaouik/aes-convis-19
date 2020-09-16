import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBanner } from 'app/shared/model/banner.model';

type EntityResponseType = HttpResponse<IBanner>;
type EntityArrayResponseType = HttpResponse<IBanner[]>;

@Injectable({ providedIn: 'root' })
export class BannerService {
  public resourceUrl = SERVER_API_URL + 'api/banners';

  constructor(protected http: HttpClient) {}

  create(banner: IBanner): Observable<EntityResponseType> {
    return this.http.post<IBanner>(this.resourceUrl, banner, { observe: 'response' });
  }

  update(banner: IBanner): Observable<EntityResponseType> {
    return this.http.put<IBanner>(this.resourceUrl, banner, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBanner>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBanner[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
