import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAnswer, Answer } from 'app/shared/model/answer.model';
import { AnswerService } from './answer.service';
import { IQuestion } from 'app/shared/model/question.model';
import { QuestionService } from 'app/entities/question/question.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = IQuestion | IUser;

@Component({
  selector: 'aes-answer-update',
  templateUrl: './answer-update.component.html',
})
export class AnswerUpdateComponent implements OnInit {
  isSaving = false;
  questions: IQuestion[] = [];
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    anwser: [null, [Validators.required]],
    comment: [null, [Validators.required]],
    date: [null, [Validators.required]],
    questionId: [],
    userId: [],
  });

  constructor(
    protected answerService: AnswerService,
    protected questionService: QuestionService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answer }) => {
      if (!answer.id) {
        const today = moment().startOf('day');
        answer.date = today;
      }

      this.updateForm(answer);

      this.questionService
        .query({ 'answerId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IQuestion[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IQuestion[]) => {
          if (!answer.questionId) {
            this.questions = resBody;
          } else {
            this.questionService
              .find(answer.questionId)
              .pipe(
                map((subRes: HttpResponse<IQuestion>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IQuestion[]) => (this.questions = concatRes));
          }
        });

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(answer: IAnswer): void {
    this.editForm.patchValue({
      id: answer.id,
      anwser: answer.anwser,
      comment: answer.comment,
      date: answer.date ? answer.date.format(DATE_TIME_FORMAT) : null,
      questionId: answer.questionId,
      userId: answer.userId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const answer = this.createFromForm();
    if (answer.id !== undefined) {
      this.subscribeToSaveResponse(this.answerService.update(answer));
    } else {
      this.subscribeToSaveResponse(this.answerService.create(answer));
    }
  }

  private createFromForm(): IAnswer {
    return {
      ...new Answer(),
      id: this.editForm.get(['id'])!.value,
      anwser: this.editForm.get(['anwser'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      questionId: this.editForm.get(['questionId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
