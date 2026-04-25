import { useEffect, useState } from "react";
import "./App.css";

import { registerUser, updateUserType } from "./api/userApi";
import { AnalyticsPreview } from "./components/AnalyticsPreview";
import { AssistantWidget } from "./components/AssistantWidget";
import { BankingDemo } from "./components/BankingDemo";
import { LandingHero } from "./components/LandingHero";
import { UserTypeSelector } from "./components/UserTypeSelector";
import { WalkthroughOverlay } from "./components/WalkthroughOverlay";
import { onboardingWalkthrough } from "./mocks/walkthroughMock";
import type { WalkthroughStep } from "./types/assistant";
import type { UserProfile, UserType } from "./types/user";

function App() {
  const [user, setUser] = useState<UserProfile | null>(null);
  const [loadingUser, setLoadingUser] = useState(false);
  const [steps, setSteps] = useState<WalkthroughStep[]>([]);
  const [activeStep, setActiveStep] = useState(0);

  useEffect(() => {
    async function createDemoUser() {
      try {
        setLoadingUser(true);

        const profile = await registerUser({
          externalId: "demo-user-assetguard",
          locale: "ro",
        });

        setUser(profile);
      } finally {
        setLoadingUser(false);
      }
    }

    createDemoUser();
  }, []);

  async function handleChangeUserType(type: UserType) {
    if (!user) return;

    try {
      setLoadingUser(true);
      const updated = await updateUserType(user.id, { userType: type });
      setUser(updated);
    } finally {
      setLoadingUser(false);
    }
  }

  function startWalkthrough(newSteps: WalkthroughStep[]) {
    setSteps(newSteps);
    setActiveStep(0);
  }

  function closeWalkthrough() {
    setSteps([]);
    setActiveStep(0);
  }

  function nextStep() {
    if (activeStep === steps.length - 1) {
      closeWalkthrough();
      return;
    }

    setActiveStep((current) => current + 1);
  }
  function prevStep() {
    setActiveStep((current) => Math.max(0, current - 1));
  }

  return (
      <div className="app-shell">
        <LandingHero
            onStartOnboarding={() => startWalkthrough(onboardingWalkthrough)}
        />

        <UserTypeSelector
            user={user}
            loading={loadingUser}
            onChangeType={handleChangeUserType}
        />

        <BankingDemo />

        <AnalyticsPreview />

        <AssistantWidget onStartWalkthrough={startWalkthrough} />

        <WalkthroughOverlay
            steps={steps}
            activeIndex={activeStep}
            onNext={nextStep}
            onPrev={prevStep}
            onClose={closeWalkthrough}
        />
      </div>
  );
}

export default App;