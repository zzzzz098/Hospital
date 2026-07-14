export interface LoginForm { account: string; password: string; role: string }
export interface RegisterForm { account: string; password: string; name: string; email: string }
export interface UserInfo { id: number; account: string; name: string; role: string; email?: string; integrity?: string; office?: string }
export interface LoginResponse { token: string; userInfo: UserInfo }
export interface Doctor { did: number; dname: string; gender: string; age: number; office: string; room: string; career: string; fee: number; description: string; picpath: string; account?: string }
export interface WorkDay { wid: number; did: number; worktime: string; ampm: string; nsnum: number; state: string }
export interface NumSource { id: number; workdayid: number; serialnumber: number; nstime: string; state: string }
export interface Appointment { rid: number; pid: number; did: number; serialnumber: number; visitdate: string; visitnoon: string; visittime: string; ordertime: string; state: string }
export interface Message { id: number; pid: number; did: number; content: string; reply: string; createTime: string; replyTime: string }
export interface MedicinePush { id: number; title: string; content: string; pushTime: string }
export interface Office { officename: string; description: string; doctornum: number }
