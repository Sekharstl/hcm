import { signIn } from "next-auth/react";

export default function Login() {
  return (
    <div style={{ padding: 40 }}>
      <h1>Sign in to your account</h1>
      <button onClick={() => signIn("okta")}>Sign in with Okta</button>
    </div>
  );
}