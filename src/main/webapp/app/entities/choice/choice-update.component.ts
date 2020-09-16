import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IChoice, Choice } from 'app/shared/model/choice.model';
import { ChoiceService } from './choice.service';
import { IQuestion } from 'app/shared/model/question.model';
import { QuestionService } from 'app/entities/question/question.service';

@Component({
  selector: 'aes-choice-update',
  templateUrl: './choice-update.component.html',
})
export class ChoiceUpdateComponent implements OnInit {
  isSaving = false;
  questions: IQuestion[] = [];

  editForm = this.fb.group({
    id: [],
    choice: [null, [Validators.required]],
    questionId: [],
  });

  constructor(
    protected choiceService: ChoiceService,
    protected questionService: QuestionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ choice }) => {
      this.updateForm(choice);

      this.questionService.query().subscribe((res: HttpResponse<IQuestion[]>) => (this.questions = res.body || []));
    });
  }

  updateForm(choice: IChoice): void {
    this.editForm.patchValue({
      id: choice.id,
      choice: choice.choice,
      questionId: choice.questionId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const choice = this.createFromForm();
    if (choice.id !== undefined) {
      this.subscribeToSaveResponse(this.choiceService.update(choice));
    } else {
      this.subscribeToSaveResponse(this.choiceService.create(choice));
    }
  }

  private createFromForm(): IChoice {
    return {
      ...new Choice(),
      id: this.editForm.get(['id'])!.value,
      choice: this.editForm.get(['choice'])!.value,
      questionId: this.editForm.get(['questionId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChoice>>): void {
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

  trackById(index: number, item: IQuestion): any {
    return item.id;
  }
}
