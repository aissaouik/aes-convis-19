import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAnswer } from 'app/shared/model/answer.model';

type EntityResponseType = HttpResponse<IAnswer>;
type EntityArrayResponseType = HttpResponse<IAnswer[]>;

@Injectable({ providedIn: 'root' })
export class AnswerService {
  public resourceUrl = SERVER_API_URL + 'api/answers';

  constructor(protected http: HttpClient) {}

  create(answer: IAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answer);
    return this.http
      .post<IAnswer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(answer: IAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answer);
    return this.http
      .put<IAnswer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnswer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(answer: IAnswer): IAnswer {
    const copy: IAnswer = Object.assign({}, answer, {
      date: answer.date && answer.date.isValid() ? answer.date.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((answer: IAnswer) => {
        answer.date = answer.date ? moment(answer.date) : undefined;
      });
    }
    return res;
  }
}
