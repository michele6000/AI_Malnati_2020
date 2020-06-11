export class Student {
  id: string;
  name: string;
  firstName: string;
  group: string;
  courseId: number;

  constructor(id: string, firstName: string, name: string, group: string, courseId: number) {
    this.id = id;
    this.name = name;
    this.firstName = firstName;
    this.group = group;
    this.courseId = courseId;
  }
}


