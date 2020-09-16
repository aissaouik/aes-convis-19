import { IChoice } from 'app/shared/model/choice.model';
import { QuestionType } from 'app/shared/model/enumerations/question-type.model';

export interface IQuestion {
  id?: number;
  title?: string;
  type?: QuestionType;
  choices?: IChoice[];
  answerId?: number;
}

export class Question implements IQuestion {
  constructor(
    public id?: number,
    public title?: string,
    public type?: QuestionType,
    public choices?: IChoice[],
    public answerId?: number
  ) {}
}
