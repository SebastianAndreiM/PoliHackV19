import { useEffect, useState } from "react";
import "./App.css";

import { getLayoutByUserType } from "./api/uiApi";
import { AnalyticsPreview } from "./components/AnalyticsPreview";
import { AssistantWidget } from "./components/AssistantWidget";
import { BankingDemo } from "./components/BankingDemo";
import { LandingHero } from "./components/LandingHero";
import { LayoutPreview } from "./components/LayoutPreview";
import { LayoutSwitcher } from "./components/LayoutSwitcher";
import { WalkthroughOverlay } from "./components/WalkthroughOverlay";
import { onboardingWalkthrough } from "./mocks/walkthroughMock";
import type { WalkthroughStep } from "./types/assistant";
import type { AdaptiveLayout, UserType } from "./types/ui";

function App() {
  const [selectedType, setSelectedType] = useState<UserType>("DEFAULT");
  const [layout, setLayout] = useState<AdaptiveLayout | null>(null);
  const [loadingLayout, setLoadingLayout] = useState(false);

  const [steps, setSteps] = useState<WalkthroughStep[]>([]);
  const [activeStep, setActiveStep] = useState(0);

  useEffect(() => {
    async function loadLayout() {
      try {
        setLoadingLayout(true);
        const data = await getLayoutByUserType(selectedType);
        setLayout(data);
      } finally {
        setLoadingLayout(false);
      }
    }

    loadLayout();
  }, [selectedType]);

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

        <LayoutSwitcher selectedType={selectedType} onChange={setSelectedType} />

        <LayoutPreview layout={layout} loading={loadingLayout} />

        <BankingDemo layout={layout} />

        <AnalyticsPreview />

        <AssistantWidget
            onStartWalkthrough={startWalkthrough}
            activeStep={steps[activeStep] ?? null}
        />

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