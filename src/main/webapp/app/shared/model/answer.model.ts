import { Moment } from 'moment';

export interface IAnswer {
  id?: number;
  anwser?: string;
  comment?: string;
  date?: Moment;
  questionId?: number;
  userId?: number;
}

export class Answer implements IAnswer {
  constructor(
    public id?: number,
    public anwser?: string,
    public comment?: string,
    public date?: Moment,
    public questionId?: number,
    public userId?: number
  ) {}
}
